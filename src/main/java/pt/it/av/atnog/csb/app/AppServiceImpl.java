package pt.it.av.atnog.csb.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileExistsException;

import pt.it.av.atnog.csb.acm.ACM;
import pt.it.av.atnog.csb.acm.git.GitAcm;
import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.csb.ACMLog;
import pt.it.av.atnog.csb.entity.csb.App;
import pt.it.av.atnog.csb.entity.csb.Instance;
import pt.it.av.atnog.csb.entity.csb.Log;
import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.Memory;
import pt.it.av.atnog.csb.entity.csb.Metric;
import pt.it.av.atnog.csb.entity.csb.MetricCollection;
import pt.it.av.atnog.csb.entity.csb.MetricEntry;
import pt.it.av.atnog.csb.entity.csb.Monitor;
import pt.it.av.atnog.csb.entity.csb.Provider;
import pt.it.av.atnog.csb.entity.csb.Service;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationCreateResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationInfoResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationLogsResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStatisticsResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStatusResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMInstance;
import pt.it.av.atnog.csb.entity.paasmanager.PMMetric;
import pt.it.av.atnog.csb.entity.paasmanager.PMMetricCollection;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceInfoResponse;
import pt.it.av.atnog.csb.paas.PaasProviderService;
import pt.it.av.atnog.csb.paas.manager.PaasProviderPTIn;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Stateless
@Path("/apps")
public class AppServiceImpl implements AppService {

	public static enum APP_STATUS {
		CREATED("created"), DEPLOYED("deployed"), STARTED("started"), STOPPED("stopped"), RESTARTED("restarted"), DELETED(
		        "deleted"), RUNNING("running");

		private final String name;

		private APP_STATUS(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	};

	@PersistenceContext(unitName = "CSBPU")
	private EntityManager em;

	/**
	 * @inheritDoc
	 */
	@Override
	public List<App> getApps() {
		TypedQuery<App> q = em.createQuery("SELECT apps from App apps", App.class);
		List<App> apps = q.getResultList();

		for (App app : apps) {
			app.setRepository(GitAcm.getRemoteRepository(app.getId()));

			try {
				app.setStatus(statusApp(app.getId()).getAppStatus());
			} catch (Exception e) {
				// most probably the app doesn't exist in the PaasM yet. ignore!
			}
		}

		return apps;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public App getApp(String appId) {
		try {
			Query q = em.createQuery("SELECT app from App app WHERE id=:appId");
			q.setParameter("appId", appId);
			App app = (App) q.getSingleResult();

			if (!appExists(app)) {
				throw new CSBException(Status.NOT_FOUND, "Application not found");
			}

			app.setRepository(GitAcm.getRemoteRepository(app.getId()));

			try {
				PMApplicationInfoResponse info = infoApp(appId);
				app.setStatus(info.getAppStatus());
				app.setInstances(info.getAppInstances());
				app.setFramework(info.getAppFramework());
				Memory memory = new Memory();
				memory.setValue(info.getAppMemory().getValue());
				memory.setUnit(info.getAppMemory().getUnit());
				app.setMemory(memory);
				app.setServicesId(info.getAppServicesId());
			} catch (Exception e) {
				// most probably the app doesn't exist in the PaasM yet. ignore!
			}
			return app;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.NOT_FOUND, "Application not found");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response createApp(String appId, Manifest manifest) {

		App app;

		// check if an app with 'appId' already exists
		app = em.find(App.class, appId);

		if (app != null) {
			throw new CSBException(Status.CONFLICT, "Application '" + appId + "' already exits");
		}

		try {
			ACM acm = new GitAcm(appId);

			if (manifest == null) {
				acm.init();
			} else {
				acm.init(manifest);
			}

			// register the new app in database
			app = new App();
			app.setId(appId);
			app.setUrl(getAppUrl(appId));
			em.persist(app);

			return Response.ok().build();
		} catch (FileExistsException e) {
			e.printStackTrace();
			throw new CSBException(Status.CONFLICT, "App '" + appId + "' already exits or hooks setup has failed");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating the app");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating the app");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response deployApp(String appId) {
		App app;
		PMApplicationCreateResponse cResponse;

		app = em.find(App.class, appId);
		if (app == null) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			ACM acm = new GitAcm(appId);
			PaasProviderService pps = new PaasProviderPTIn();
			Manifest manifest = acm.getManifest();

			if (app.getProvider() == null) {
				// app is not yet created in the PaaS Manager. create it first!
				cResponse = pps.createApp(appId, manifest.getProvider(), manifest.getFramework());
			}

			// deploy!
			cResponse = pps.deployApp(appId, acm.getData());

			// by this time the app was successfully created and deployed
			Provider provider = em.find(Provider.class, cResponse.getPaasProvider());
			if (provider == null) {
				// provider doesn't exist yet in database. insert it!
				provider = new Provider();
				provider.setId(cResponse.getPaasProvider());
				em.persist(provider);
			}

			// set app's provider
			app.setProvider(provider);
			app.setUrl(cResponse.getAppUrl()); // FIXME in a future CSB version this will not be necessary any longer
			em.persist(app);

			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while deploying the app1.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while deploying the app2.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while deploying the app3.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response startApp(String appId) {
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			pps.startApp(appId);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while starting the app.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while starting the app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response stopApp(String appId) {
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			pps.stopApp(appId);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while stopping the app.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while stopping the app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response restartApp(String appId) {
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			pps.restartApp(appId);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while restarting the app.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while restarting the app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response deleteApp(String appId) {
		try {

			App app = em.find(App.class, appId);

			if (app == null) {
				throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
			}

			if (app.getProvider() != null) {
				// app is deployed in a provider
				PaasProviderService pps = new PaasProviderPTIn();
				pps.deleteApp(appId);
			}

			ACM acm = new GitAcm(appId);
			acm.deleteApp(appId);
			em.remove(app);

			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting the app.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting the app.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting the app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public PMApplicationStatusResponse statusApp(String appId) throws CSBException {
		PMApplicationStatusResponse response = new PMApplicationStatusResponse(appId);

		App app = em.find(App.class, appId);
		if (!appExists(app)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		// response.setAppUrl(getAppUrl(appId)); // FIXME
		response.setAppUrl(app.getUrl());

		if (app.getProvider() == null) {
			response.setAppStatus(APP_STATUS.CREATED.toString());
			return response;
		}

		try {
			PaasProviderService pps = new PaasProviderPTIn();
			response = pps.statusApp(appId);
			return response;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting status of the app.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting status of the app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ACMLog commitLogApp(String appId) {
		try {
			ACM acm = new GitAcm(appId);
			return acm.getLog();
		} catch (IOException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting the log of the app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public PMApplicationInfoResponse infoApp(String appId) {
		PMApplicationInfoResponse response;
		if (appExistsUpstream(appId)) {
			try {
				PaasProviderService pps = new PaasProviderPTIn();
				response = pps.infoApp(appId);
				response.setAppUrl(em.find(App.class, appId).getUrl());
			} catch (ConfigurationException e) {
				e.printStackTrace();
				throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting app info.");
			}
		} else {
			response = new PMApplicationInfoResponse(appId);
			response.setAppInstances(0);
			response.setAppStatus(APP_STATUS.CREATED.toString());
			response.setAppUrl(getAppUrl(appId));
		}

		return response;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response scaleApp(String appId, int nInstances) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			pps.scaleApp(appId, nInstances);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while scalling app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public App migrateApp(String appId, String providerId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			PMApplicationCreateResponse response = pps.migrateApp(appId, providerId);
			App app = em.find(App.class, appId);
			app.setUrl(response.getAppUrl());
			em.persist(app);
			return app;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while scalling app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response createService(String appId, String serviceId, String vendorId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			pps.createService(appId, serviceId, vendorId);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while creating service for app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response deleteService(String appId, String csbServiceId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			pps.deleteService(appId, csbServiceId);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while deleting service for app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Service getService(String appId, String csbServiceId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			PMServiceInfoResponse response = pps.getService(appId, csbServiceId);
			Service service = new Service();
			service.setId(csbServiceId);
			service.setVendorId(response.getServiceVendor());
			service.setUrl(response.getServiceUrl());
			service.setUsername(response.getServiceUsername());
			service.setPassword(response.getServicePassword());
			return service;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while deleting service for app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<Service> getServices(String appId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			List<PMServiceInfoResponse> response = pps.getServices(appId).getServices();
			List<Service> services = new ArrayList<Service>();

			if (response != null) {
				Service service;

				for (PMServiceInfoResponse s : response) {
					service = new Service();
					service.setId(s.getServiceId());
					service.setVendorId(s.getServiceVendor());
					service.setUrl(s.getServiceUrl());
					service.setUsername(s.getServiceUsername());
					service.setPassword(s.getServicePassword());
					services.add(service);
				}
			}

			return services;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while deleting service for app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Log logApp(String appId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderService pps = new PaasProviderPTIn();
			PMApplicationLogsResponse response = pps.logsApp(appId);
			Log log = new Log();
			log.setAppId(appId);
			log.setProvider(response.getPaasProvider());
			log.setMessage(response.getAppLog());
			// response.setAppUrl(getAppUrl(appId)); // FIXME
			log.setUrl(em.find(App.class, appId).getUrl());
			return log;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while getting app logs.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while getting app logs.");
		}
	}

	@Override
	public Monitor monitorApp(String appId, int samples) {
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			PMApplicationStatisticsResponse stats = pps.statisticsApp(appId, samples);
			Monitor monitor = new Monitor();
			monitor.setAppId(appId);
			
			List<Instance> instances = new ArrayList<Instance>();
			Instance instance;
			Metric metric;
			
			HashMap<String, Metric> hashMetrics;
			
			
			for (PMInstance i : stats.getInstances()) {
				instance = new Instance();
				instance.setId(i.getId());
				
				hashMetrics = new HashMap<String, Metric>();
				
				for (PMMetricCollection mc : i.getMetricCollections()) {
					for (PMMetric m : mc.getMetrics()) {
						if (!hashMetrics.containsKey(m.getName())) {
							hashMetrics.put(m.getName(), new Metric(m.getName(), m.getInfo(), m.getUnit()));
						}
						
						metric = hashMetrics.get(m.getName());
						if (metric.getMetricEntries() == null) {
							metric.setMetricEntries(new ArrayList<MetricEntry>());
						}
						
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
						Date convertedDate = new Date();
						convertedDate = dateFormat.parse(mc.getDate());
						metric.getMetricEntries().add(new MetricEntry(m.getValue(), Long.toString(convertedDate.getTime() / 1000)));
					}
				}
				
				instance.setMetrics(new ArrayList<Metric>(hashMetrics.values()));
				instances.add(instance);
			}
			
			monitor.setInstances(instances);
			

//			List<Instance> instances = new ArrayList<Instance>();
//			List<MetricCollection> metricCollections = new ArrayList<MetricCollection>();
//			List<Metric> metrics;
//			MetricCollection metricCollection;
//			Instance instance;
//			Metric metric;
//
//			for (PMInstance i : stats.getInstances()) {
//				instance = new Instance();
//				instance.setId(i.getId());
//
//				for (PMMetricCollection mc : i.getMetricCollections()) {
//					metricCollection = new MetricCollection();
//					metricCollection.setDate(mc.getDate());
//
//					metrics = new ArrayList<Metric>();
//
//					for (PMMetric m : mc.getMetrics()) {
//						metric = new Metric();
//						metric.setName(m.getName());
//						metric.setInfo(m.getInfo());
//						metric.setValue(m.getValue());
//						metric.setUnit(m.getUnit());
//						metrics.add(metric);
//					}
//
//					metricCollection.setMetrics(metrics);
//					metricCollections.add(metricCollection);
//				}
//
//				instance.setMetricCollection(metricCollections);
//				instances.add(instance);
//			}
//
//			monitor.setInstances(instances);
			return monitor;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while getting app monitoring data.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while getting app monitoring data.");
		}
	}

	private String getAppUrl(String appId) {
		return "http://" + appId + ".csb.atnog.av.it.pt"; // FIXME
	}

	private boolean appExists(String appId) {
		return appExists(em.find(App.class, appId));
	}

	private boolean appExists(App app) {
		return app != null;
	}

	private boolean appExistsUpstream(String appId) {
		App app = em.find(App.class, appId);
		if (app != null)
			return appExistsUpstream(app);
		return false;
	}

	private boolean appExistsUpstream(App app) {
		return app.getProvider() != null;
	}

}
