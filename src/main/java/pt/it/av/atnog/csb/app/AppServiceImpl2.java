package pt.it.av.atnog.csb.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.resteasy.spi.NotFoundException;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.it.av.atnog.csb.acm.ACM2;
import pt.it.av.atnog.csb.acm.ACM2Impl;
import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.csb.App;
import pt.it.av.atnog.csb.entity.csb.Instance;
import pt.it.av.atnog.csb.entity.csb.Log;
import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.Memory;
import pt.it.av.atnog.csb.entity.csb.Metric;
import pt.it.av.atnog.csb.entity.csb.MetricEntry;
import pt.it.av.atnog.csb.entity.csb.Monitor;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;
import pt.it.av.atnog.csb.entity.csb.Service;
import pt.it.av.atnog.csb.entity.csb.User;
import pt.it.av.atnog.csb.entity.csb.App.DeployState;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationCreateResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationInfoResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationLogsResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStatisticsResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStatusResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMInstance;
import pt.it.av.atnog.csb.entity.paasmanager.PMMetric;
import pt.it.av.atnog.csb.entity.paasmanager.PMMetricCollection;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceInfoResponse;
import pt.it.av.atnog.csb.entity.ppm.PrivatePaas;
import pt.it.av.atnog.csb.entity.ppm.PrivatePaas.State;
import pt.it.av.atnog.csb.paas.PaasProviderService;
import pt.it.av.atnog.csb.paas.manager.PaasProviderPTIn;
import pt.it.av.atnog.csb.paas.manager.PrivatePaasManager;
import pt.it.av.atnog.csb.scheduler.CsbScheduler;
import pt.it.av.atnog.csb.scheduler.jobs.MachineJob;
import pt.it.av.atnog.csb.user.UserResource;
import pt.it.av.atnog.csb.user.UserResourceImpl;
import sonia.scm.repository.Permission;
import sonia.scm.repository.PermissionType;
import sonia.scm.repository.Repository;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Stateless
@Path("/apps")
public class AppServiceImpl2 implements AppService2 {
	
	private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl2.class);

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
	
	@EJB
	CsbScheduler scheduler;
	
	/**
	 * @inheritDoc
	 */
	@Override
	public List<App> getApps(SecurityContext ctx) {
		List<App> apps = em.createNamedQuery("App.findAllByUserName", App.class)
							.setParameter("user_name", ctx.getUserPrincipal().getName())
							.getResultList();

		for (App app : apps) {
			app.setRepository(getUrl(app));

			try {
				app.setStatus(statusApp(ctx, app.getId()).getAppStatus());
			} catch (Exception e) {
				// most likely the app doesn't exist in the PaasM yet. ignore!
			}
		}

		return apps;
	}
	
	private String getUrl(String appId, String repositoryType) {
		ACM2 acm = ACM2Impl.getInstance();
		return acm.getUrl(appId, repositoryType);
	}
	
	private String getUrl(App app) {
		return getUrl(app.getId(), app.getRepositoryType());
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public App getApp(SecurityContext ctx, String appId) {
		try {
			App app = getAppById(appId);
			app.setRepository(getUrl(app));

			try {
				PMApplicationInfoResponse info = infoApp(ctx, appId);
				app.setStatus(info.getStatus());
				app.setInstances(info.getInstances());
				app.setFrameworkId(info.getFrameworkId());
				Memory memory = new Memory();
				memory.setValue(info.getMemory().getValue());
				memory.setUnit(info.getMemory().getUnit());
				app.setMemory(memory);
				app.setServicesId(info.getServicesId());
			} catch (Exception e) {
				// most likely the app doesn't exist in the PaasM yet. ignore!
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
	public Response createApp(SecurityContext ctx, String appId, String repositoryType) {
		// check if an app with 'appId' already exists
		try {
			getAppById(appId);
			throw new CSBException(Status.CONFLICT, "Application '" + appId + "' already exits");
		} catch (NoResultException e) {
			// app doesn't exist yet. continue.
		}

		try {
			UserResource ur = new UserResourceImpl(em);
			User user = ur.getUser(ctx, ctx.getUserPrincipal().getName());

			// Repository info
			Repository repository = new Repository();
			repository.setName(appId);
			repository.setType(repositoryType);
			
			// Set permissions for user to owner
			List<Permission> permissions = new ArrayList<Permission>();
			permissions.add(new Permission(user.getName(), PermissionType.OWNER));
			permissions.add(new Permission("csb", PermissionType.WRITE)); // TODO load user from acm2.properties or retrieve from ACM2
			repository.setPermissions(permissions);
			
			// Create the SCM repository
			ACM2 acm2 = ACM2Impl.getInstance();
			acm2.createRepository(repository);
			
			// App info
			App app = new App(appId, user);
			app.setUrl(getAppUrl(appId));
			app.setRepositoryType(repositoryType);
			em.persist(app);
			
			return Response.ok().build();
		} catch (Exception e) {
			logger.error("Error while creating app {}. Stack trace is: {}", new Object[] { appId, e.getMessage() });
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating the app");
		}
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public Response createManifest(SecurityContext ctx, String appId, Manifest manifest) {
		App app = checkAppExistsAndAuthorization(ctx, appId);
		Repository repository = getAcm2().getRepository(app.getId(), app.getRepositoryType());
		getAcm2().addManifest(repository, manifest);
		return Response.ok().build();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response deployApp(SecurityContext ctx, String appId) {
		PMApplicationCreateResponse cResponse;

		App app = em.find(App.class, appId);
		if (app == null) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			ACM2 acm2 = getAcm2();
			PaasProviderService pps = new PaasProviderPTIn(em);
			Manifest manifest = acm2.getManifest(acm2.getRepository(appId, app.getRepositoryType()));
			PaasProvider provider = null;
			PrivatePaas pp = null;
			
			// check if app is to be deployed in a private PaaS
			PrivatePaasManager ppm = new PrivatePaasManager();
			List<PrivatePaas> ppNotBootstrapped = ppm.offeringsUnbootstrapped(em);
			
			for (PrivatePaas p : ppNotBootstrapped) {
				logger.debug("Comparing {} with {}", p.getId(), manifest.getProvider());
				if (p.getId().equals(manifest.getProvider())) {
					logger.debug("{} equals {}", p.getId(), manifest.getProvider());
					p.setState(State.BOOTSTRAPPING); // set state as bootstrapping
					provider = p;
					pp = p;
					break;
				}
			}
			
			if (provider != null) {
				logger.debug("Scheduling a new machine job...");
				// user requested app to be deployed to an inexistent private PaaS - bootstrap it!
				JobDetail jobMachine = new JobDetailImpl()
									.getJobBuilder()
										.withIdentity("machine_job")
										.ofType(MachineJob.class)
									.build();
				
				jobMachine.getJobDataMap().put("paas_id", provider.getId());
				jobMachine.getJobDataMap().put("domain", pp.getDomain());
				jobMachine.getJobDataMap().put("callback", "http://10.115.1.19:8080/csb/rest/ppm/register?id=" + provider.getId()); // FIXME
				jobMachine.getJobDataMap().put("scheduler", scheduler.getScheduler());
		    	Trigger trigger = new SimpleTriggerImpl()
		    						.getTriggerBuilder()
		    							.withIdentity("machine_trigger")
		    							.startNow()
		    						.build();
		    	scheduler.getScheduler().scheduleJob(jobMachine, trigger);
		    	logger.debug("Machine job scheduled and fired off!");
		    	
		    	app.setDeployState(DeployState.PENDING);
		    	em.persist(app);
			} else {
				// check if app is to be deployed in an already existent PaaS
				for (PaasProvider p : pps.getAllPaas()) {
					if (p.getId().equals(manifest.getProvider())) {
						provider = p;
						break;
					}
				}

				if (provider == null) {
						throw new CSBException(Status.BAD_REQUEST, "Chosen PaaS provider does not exist.");
				} else {
					logger.info("Deploying app {} to PaaS {}...", app.getId(), provider.getId());
				}
				
				app.setDeployState(DeployState.DEPLOYING);
				em.persist(app);
				
				if (app.getProvider() == null) {
					logger.info("First app {} deploy (provider is still unset). Let's create the app in the PaaS {} first...", app.getId(), provider.getId());
					// app is not yet created in the PaaS Manager. create it first!
					cResponse = pps.createApp(appId, manifest.getProvider(), manifest.getFramework());
					logger.info("Created app {} in PaaS {}", app.getId(), provider.getId());
				}

				// deploy!
				File zipFile = acm2.zipRepository(getAcm2().getRepository(appId, app.getRepositoryType()));
				try {
					InputStream is = new FileInputStream(zipFile);
					logger.info("Deploying app {} to PaaS {}", app.getId(), provider.getId());
					cResponse = pps.deployApp(appId, is);
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Error with intput stream");
				} finally {
					zipFile.delete();
						
				}
	
				// by this time the app was successfully created and deployed
				provider = em.find(PaasProvider.class, cResponse.getPaasProvider());
				if (provider == null) {
					// provider doesn't exist yet in database. insert it!
					logger.debug("provider {} doesn't exist yet in database. insert it!", cResponse.getPaasProvider());
					provider = new PaasProvider();
					provider.setId(cResponse.getPaasProvider());
					em.persist(provider);
				}
	
				// set app's provider
				app.setProvider(provider);
				app.setUrl(cResponse.getAppUrl());
				app.setDeployState(DeployState.DEPLOYED);
				em.persist(app);
				logger.info("App {} successfully deployed to PaaS {}", app.getId(), provider.getId());
			}

			return Response.status(Status.ACCEPTED).build();
//		} catch (ConfigurationException e) {
//			e.printStackTrace();
//			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while deploying app.");
		} catch (FileNotFoundException e) {
			logger.debug("Manifest file not found for app {}", app.getId());
			throw new CSBException(Status.PRECONDITION_FAILED, "Manifest file not found");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while deploying app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response startApp(SecurityContext ctx, String appId) {
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
	public Response stopApp(SecurityContext ctx, String appId) {
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
	public Response restartApp(SecurityContext ctx, String appId) {
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
	public Response deleteApp(SecurityContext ctx, String appId) {
		try {
			logger.debug("Deleting app id {}", appId);
			App app = em.find(App.class, appId);

			if (app == null) {
				throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
			}

			if (app.getProvider() != null) {
				// app is deployed in a provider
				PaasProviderService pps = new PaasProviderPTIn();
				pps.deleteApp(appId);
			}
			
			logger.debug("Deleting repository of app ID" + appId);
			Repository repository = getAcm2().getRepository(appId, app.getRepositoryType());
			getAcm2().deleteRepository(repository);
			logger.debug("Done deleting repository of app ID" + appId);
			em.remove(app);

			return Response.ok().build();
		} catch (ConfigurationException e) {
			logger.error("Error while deleting app {} due to configuration errors. Stack trace is: {}", new Object[] { appId, e.getMessage() });
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting the app.");
		} catch (Exception e) {
			logger.error("Error while deleting app {}. Stack trace is: {}", new Object[] { appId, e.getMessage() });
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting the app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public PMApplicationStatusResponse statusApp(SecurityContext ctx, String appId) throws CSBException {
		App app = getAppById(appId);
		
		PMApplicationStatusResponse response = new PMApplicationStatusResponse(appId);
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
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting status of the app.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting status of the app.");
		}
	}

//	/**
//	 * @inheritDoc
//	 */
//	@Override
//	public ACMLog commitLogApp(SecurityContext ctx, String appId) {
//		App app = em.find(App.class, appId);
//		if (!appExists(app)) {
//			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
//		}
//		
//		try {
//			ACM acm = new GitAcm(app);
//			return acm.getLog();
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting the log of the app.");
//		}
//	}

	/**
	 * @inheritDoc
	 */
	@Override
	public PMApplicationInfoResponse infoApp(SecurityContext ctx, String appId) {
		PMApplicationInfoResponse response;
		if (appExistsUpstream(appId)) {
			try {
				PaasProviderService pps = new PaasProviderPTIn();
				response = pps.infoApp(appId);
				response.setUrl(em.find(App.class, appId).getUrl());
			} catch (ConfigurationException e) {
				e.printStackTrace();
				throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting app info.");
			}
		} else {
			response = new PMApplicationInfoResponse(appId);
			response.setInstances(0);
			response.setStatus(APP_STATUS.CREATED.toString());
			response.setUrl(getAppUrl(appId));
		}

		return response;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response scaleApp(SecurityContext ctx, String appId, int nInstances) {
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
	public App migrateApp(SecurityContext ctx, String appId, String providerId) {
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
	public Response createService(SecurityContext ctx, String appId, String serviceId, String vendorId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			pps.createService(appId, serviceId, vendorId);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating service for app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Response deleteService(SecurityContext ctx, String appId, String csbServiceId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			pps.deleteService(appId, csbServiceId);
			return Response.ok().build();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting service for app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Service getService(SecurityContext ctx, String appId, String csbServiceId) {
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
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting service for app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<Service> getServices(SecurityContext ctx, String appId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}
		
		if (!appExistsUpstream(appId)) {
			return new ArrayList<Service>(); // empty list
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
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting app services.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Log logApp(SecurityContext ctx, String appId) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}
		
		if (!appExistsUpstream(appId)) {
			return new Log();
		}

		try {
			PaasProviderService pps = new PaasProviderPTIn();
			PMApplicationLogsResponse response = pps.logsApp(appId);
			Log log = new Log();
			log.setAppId(appId);
			log.setProvider(response.getPaasProvider());
			log.setMessage(response.getAppLog());
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
	public Monitor monitorApp(SecurityContext ctx, String appId, int samples) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}
		
		if (!appExistsUpstream(appId)) {
			return new Monitor();
		}
		
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
			return monitor;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while getting app monitoring data.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal Server Error while getting app monitoring data.");
		}
	}
	
	private App getAppById(String appId) throws NoResultException {
		return em.createNamedQuery("App.findById", App.class).setParameter("id", appId).getSingleResult();
	}

	private String getAppUrl(String appId) {
		return "http://" + appId + ".csb.atnog.av.it.pt"; // FIXME move this to config file
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
	
	private ACM2 getAcm2() {
		return ACM2Impl.getInstance();
	}
	
	private App checkAppExistsAndAuthorization(SecurityContext ctx, String appId) {
		try {
			App app = getAppById(appId);
			if (app.getUser().getName().equals(ctx.getUserPrincipal().getName())) {
				return app;
			}
			throw new UnauthorizedException();
		} catch (NoResultException e) {
			throw new NotFoundException("Application not found");
		}
	}

	@Override
	public List<App> getAppsAsync() {
		try {
			Thread.sleep(1*60*1000); // 1 minute
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		return new ArrayList<App>();
	}
}
