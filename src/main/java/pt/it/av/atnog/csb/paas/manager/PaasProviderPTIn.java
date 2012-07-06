package pt.it.av.atnog.csb.paas.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import pt.it.av.atnog.csb.entity.common.ApplicationCreateResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationDeleteResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationInfoResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationRestartResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationScaleResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStartResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStatusResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStopResponse;
import pt.it.av.atnog.csb.entity.common.PaasProviders;
import pt.it.av.atnog.csb.entity.common.ServiceCreateResponse;
import pt.it.av.atnog.csb.entity.common.ServiceDeleteResponse;
import pt.it.av.atnog.csb.exception.CSBException;
import pt.it.av.atnog.csb.paas.PaasProviderService;

/**
 * PT Inovacao PaaS Manager
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Stateless
public class PaasProviderPTIn implements PaasProviderService {

	private PropertiesConfiguration propConfig;
	public static final String PAAS_MANAGER_PTIN_PROPERTIES_FILE_NAME = "/pt/it/av/atnog/csb/paas/manager/pm-ptin.properties";

	private String pmServerUri;
	private String pmInfoGetPaasUri;
	private String pmAppsCreateAppUri;
	private String pmAppsDeployAppUri;
	private String pmAppsStartAppUri;
	private String pmAppsStopAppUri;
	private String pmAppsRestartAppUri;
	private String pmAppsDeleteAppUri;
	private String pmAppsScaleAppUri;
	private String pmInfoAppUri;
	private String pmInfoAppStatusUri;
	private String pmCreateServiceUri;
	private String pmDeleteServiceUri;

	public PaasProviderPTIn() throws ConfigurationException {
		loadConfig();
	}

	@Override
	public PaasProviders getAllPaas() {

		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest request = cFactory.createRequest(pmInfoGetPaasUri);
		request.accept(MediaType.APPLICATION_XML);
		ClientResponse<PaasProviders> response;
		
        try {
	        response = request.get(PaasProviders.class);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting the list of PaaS providers");
        }

		if (response.getStatus() != 200) {
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting the list of PaaS providers");
		}
		
		return response.getEntity();
	}

	@Override
	public ApplicationCreateResponse createApp(String appId, String provider, String framework) {
		String uri = getUriByQuery(pmAppsCreateAppUri, provider.toLowerCase(), "csb-"+appId, framework); // FIXME
		try {
	        return postUri(ApplicationCreateResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating the app.");
        }
	}

	@Override
	public ApplicationCreateResponse deployApp(String appId, InputStream data) {
		try {		
			// Delegate the deployment process to the PaaS Manager
			ClientRequestFactory cFactory = new ClientRequestFactory();
			ClientRequest cRequest = cFactory.createRequest(pmAppsDeployAppUri);
	
			MultipartFormDataOutput form = new MultipartFormDataOutput();
			form.addFormData("appID", "csb-"+appId, MediaType.TEXT_PLAIN_TYPE); // FIXME
			form.addFormData("appData", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			cRequest.body(MediaType.MULTIPART_FORM_DATA_TYPE, form);
			
			return cRequest.post(ApplicationCreateResponse.class).getEntity();
        } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deploying the app.");
        } catch (IOException e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deploying the app.");
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deploying the app.");
        }
	}

	@Override
	public ApplicationStartResponse startApp(String appId) {
		String uri = getUriByQuery(pmAppsStartAppUri, "csb-"+appId); // FIXME
		try {
	        return postUri(ApplicationStartResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while starting the app.");
        }
	}

	@Override
	public ApplicationStopResponse stopApp(String appId) {
		String uri = getUriByQuery(pmAppsStopAppUri, "csb-"+appId); // FIXME
		try {
	        return postUri(ApplicationStopResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while stopping the app.");
        }
	}

	@Override
	public ApplicationRestartResponse restartApp(String appId) {
		String uri = getUriByQuery(pmAppsRestartAppUri, "csb-"+appId); // FIXME
		try {
	        return postUri(ApplicationRestartResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while restarting the app.");
        }
	}

	@Override
	public ApplicationDeleteResponse deleteApp(String appId) {
		String uri = getUriByQuery(pmAppsDeleteAppUri, "csb-"+appId); // FIXME
		try {
	        return deleteUri(ApplicationDeleteResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting the app.");
        }
	}

	@Override
	public ApplicationStatusResponse statusApp(String appId) {
		String uri = getUriByQuery(pmInfoAppStatusUri, "csb-"+appId); // FIXME
		try {
	        return getUri(ApplicationStatusResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting status of the app.");
        }
	}
	
	@Override
    public ApplicationScaleResponse scaleApp(String appId, int nInstances) {
		String uri = getUriByQuery(pmAppsScaleAppUri, "csb-"+appId, Integer.toString(nInstances));
		try {
	        return postUri(ApplicationScaleResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while scaling the app.");
        }
    }
	
	@Override
    public ApplicationInfoResponse infoApp(String appId) {
		String uri = getUriByQuery(pmInfoAppUri, "csb-"+appId);
		try {
	        return getUri(ApplicationInfoResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting app information.");
        }
    }
	
	@Override
    public ServiceCreateResponse createService(String appId, String serviceId, String serviceName) {
		String uri = getUriByQuery(pmCreateServiceUri, "csb-"+appId, serviceName, serviceId); // FIXME
		try {
	        return postUri(ServiceCreateResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating service.");
        }
    }
	
	@Override
    public ServiceDeleteResponse deleteService(String appId, String serviceName) {
		String uri = getUriByQuery(pmDeleteServiceUri, "csb-"+appId, serviceName); // FIXME
		try {
	        return deleteUri(ServiceDeleteResponse.class, uri);
        } catch (Exception e) {
	        e.printStackTrace();
	        throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting service.");
        }
    }

	private static String getUriByQuery(String uri, String... args) {
	    return MessageFormat.format(uri, (Object[]) args);
    }
	
	private static <T> T getUri(Class<T> returnType, String uri) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		ClientResponse<T> cResponse = cRequest.get(returnType);
		return cResponse.getEntity();
	}
	
	private static <T> T postUri(Class<T> returnType, String uri) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		ClientResponse<T> cResponse = cRequest.post(returnType);
		return cResponse.getEntity();
	}
	
	private static <T> T deleteUri(Class<T> returnType, String uri) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		ClientResponse<T> cResponse = cRequest.delete(returnType);
		return cResponse.getEntity();
	}

	private Object postFormUrlEncoded(String uri, Map<String, Object> params) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			cRequest.formParameter(entry.getKey(), entry.getValue());
		}

		ClientResponse<Object> cResponse = cRequest.post(Object.class);

		return cResponse.getEntity();
	}

//	private String getAppUrl(String appId) {
//		return appId + ".csb.atnog.av.it.pt"; // FIXME
//	}

	private void loadConfig() throws ConfigurationException {
		propConfig = new PropertiesConfiguration();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PAAS_MANAGER_PTIN_PROPERTIES_FILE_NAME);
		propConfig.load(inputStream);

		pmServerUri = propConfig.getString("pm_server_uri");

		pmAppsCreateAppUri = pmServerUri + propConfig.getString("pm_apps_createapp_uri");
		pmAppsDeployAppUri = pmServerUri + propConfig.getString("pm_apps_deployapp_uri");
		pmAppsStartAppUri = pmServerUri + propConfig.getString("pm_apps_startapp_uri");
		pmAppsStopAppUri = pmServerUri + propConfig.getString("pm_apps_stopapp_uri");
		pmAppsRestartAppUri = pmServerUri + propConfig.getString("pm_apps_restartapp_uri");
		pmAppsDeleteAppUri = pmServerUri + propConfig.getString("pm_apps_deleteapp_uri");
		pmAppsScaleAppUri = pmServerUri + propConfig.getString("pm_apps_scaleapp_uri");
		pmInfoAppStatusUri = pmServerUri + propConfig.getString("pm_info_getappstatus_uri");
		pmInfoAppUri = pmServerUri + propConfig.getString("pm_info_getappinfo_uri");
		pmInfoGetPaasUri = pmServerUri + propConfig.getString("pm_info_getpaas_uri");
		
		pmCreateServiceUri = pmServerUri + propConfig.getString("pm_service_create_uri");
		pmDeleteServiceUri = pmServerUri + propConfig.getString("pm_service_delete_uri");
	}
}
