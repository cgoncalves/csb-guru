package pt.it.av.atnog.csb.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileExistsException;

import pt.it.av.atnog.csb.acm.ACM;
import pt.it.av.atnog.csb.acm.git.GitAcm;
import pt.it.av.atnog.csb.entity.common.ApplicationCreateResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationDeleteResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationDeployResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationInfoResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationRestartResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationScaleResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStartResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStatusResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStopResponse;
import pt.it.av.atnog.csb.entity.common.Provider;
import pt.it.av.atnog.csb.entity.common.ServiceCreateResponse;
import pt.it.av.atnog.csb.entity.common.ServiceDeleteResponse;
import pt.it.av.atnog.csb.entity.csb.ACMLog;
import pt.it.av.atnog.csb.entity.csb.App;
import pt.it.av.atnog.csb.entity.csb.Apps;
import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.exception.CSBException;
import pt.it.av.atnog.csb.paas.PaasProviderService;
import pt.it.av.atnog.csb.paas.manager.PaasProviderPTIn;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
@Stateless
@Path("/apps")
public class AppServiceImpl implements AppService {
	
	public static enum APP_STATUS {
		CREATED("created"),
		DEPLOYED("deployed"),
		STARTED("started"),
		STOPPED("stopped"),
		RESTARTED("restarted"),
		DELETED("deleted"),
		RUNNING("running");
		
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
	public Apps getApps() {
		TypedQuery<App> q = em.createQuery("SELECT apps from App apps", App.class);
		Apps apps = new Apps();
		apps.setApps(q.getResultList());

		for (App app : apps.getApps()) {
			app.setRepository(GitAcm.getRemoteRepository(app.getName()));

			try {
				app.setStatus(statusApp(app.getName()).getAppStatus());
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
			Query q = em.createQuery("SELECT app from App app WHERE name=:appId");
			q.setParameter("appId", appId);
			App app = (App) q.getSingleResult();

			if (!appExists(app)) {
				throw new CSBException(Status.NOT_FOUND, "Application not found");
			}

			app.setRepository(GitAcm.getRemoteRepository(app.getName()));

			try {
				app.setStatus(statusApp(app.getName()).getAppStatus());
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
	public ApplicationCreateResponse createApp(String appId, Manifest manifest) {

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
			app.setName(appId);
			app.setUrl(getAppUrl(appId));
			em.persist(app);

			ApplicationCreateResponse response = new ApplicationCreateResponse(appId);
			response.setAppStatus(APP_STATUS.CREATED.toString());
			response.setAppUrl(response.getAppUrl());
			return response;
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
	public ApplicationDeployResponse deployApp(String appId) {
		App app;
		ApplicationCreateResponse cResponse;

		app = em.find(App.class, appId);
		if (app == null) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			ACM acm = new GitAcm(appId);
			PaasProviderService pps = new PaasProviderPTIn();

			if (app.getProvider() == null) {
				// app is not yet created in the PaaS Manager. create it first!
				cResponse = pps.createApp(appId, acm.getManifest().getProvider(), acm.getManifest().getFramework()
				        .toString());
			}

			// deploy!
			cResponse = pps.deployApp(appId, acm.getData());

			// by this time the app was successfully created and deployed
			Provider provider = em.find(Provider.class, cResponse.getPaasProvider());
			if (provider == null) {
				// provider doesn't exist yet in database. insert it!
				provider = new Provider();
				provider.setName(cResponse.getPaasProvider());
				em.persist(provider);
			}

			// set app's provider
			app.setProvider(provider);
			app.setUrl(cResponse.getAppUrl()); // FIXME in a future CSB version
											   // this will not be necessary any
											   // longer
			em.persist(app);

			ApplicationDeployResponse response = new ApplicationDeployResponse(appId);
			response.setAppStatus(APP_STATUS.DEPLOYED.toString());
			// response.setAppUrl(getAppUrl(appId)); // FIXME in a future CSB
			// version this WILL be necessary
			response.setAppUrl(cResponse.getAppUrl());
			response.setPaasProvider(cResponse.getPaasProvider());
			return response;
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
	public ApplicationStartResponse startApp(String appId) {
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			ApplicationStartResponse response = pps.startApp(appId);
			response.setAppStatus(APP_STATUS.STARTED.toString());
			// response.setAppUrl(getAppUrl(appId)); // FIXME
			response.setAppUrl(em.find(App.class, appId).getUrl());
			return response;
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
	public ApplicationStopResponse stopApp(String appId) {
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			ApplicationStopResponse response = pps.stopApp(appId);
			response.setAppStatus(APP_STATUS.STOPPED.toString());
			// response.setAppUrl(getAppUrl(appId)); // FIXME
			response.setAppUrl(em.find(App.class, appId).getUrl());
			return response;
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
	public ApplicationRestartResponse restartApp(String appId) {
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			ApplicationRestartResponse response = pps.restartApp(appId);
			response.setAppStatus(APP_STATUS.RESTARTED.toString());
			// response.setAppUrl(getAppUrl(appId)); // FIXME
			response.setAppUrl(em.find(App.class, appId).getUrl());
			return response;
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
	public ApplicationDeleteResponse deleteApp(String appId) {
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

			ApplicationDeleteResponse response = new ApplicationDeleteResponse(appId);
			response.setAppStatus(APP_STATUS.DELETED.toString());
			// response.setAppUrl(getAppUrl(appId)); // FIXME
			response.setAppUrl(app.getUrl());
			return response;
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
	public ApplicationStatusResponse statusApp(String appId) throws CSBException {
		ApplicationStatusResponse response = new ApplicationStatusResponse(appId);

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
	public ACMLog logApp(String appId) {
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
	public ApplicationInfoResponse infoApp(String appId) {
		ApplicationInfoResponse response;
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
			response = new ApplicationInfoResponse(appId);
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
	public ApplicationScaleResponse scaleApp(String appId, int nInstances) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		ApplicationScaleResponse response;

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			response = pps.scaleApp(appId, nInstances);
			response.setAppUrl(em.find(App.class, appId).getUrl());
			return response;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while scalling app.");
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ServiceCreateResponse createService(String appId, String serviceId, String serviceName) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			ServiceCreateResponse response = pps.createService(appId, serviceId, serviceName);
			return response;
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
	public ServiceDeleteResponse deleteService(String appId, String serviceName) {
		if (!appExists(appId)) {
			throw new CSBException(Status.NOT_FOUND, "Application '" + appId + "' not found.");
		}

		try {
			PaasProviderPTIn pps = new PaasProviderPTIn();
			ServiceDeleteResponse response = pps.deleteService(appId, serviceName);
			return response;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while deleting service for app.");
		}
	}

	private String getAppUrl(String appId) {
		return appId + ".csb.atnog.av.it.pt"; // FIXME
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
