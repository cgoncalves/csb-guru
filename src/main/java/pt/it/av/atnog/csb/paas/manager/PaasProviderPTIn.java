package pt.it.av.atnog.csb.paas.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.csb.Framework;
import pt.it.av.atnog.csb.entity.csb.Metric;
import pt.it.av.atnog.csb.entity.csb.MetricEntry;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;
import pt.it.av.atnog.csb.entity.csb.Runtime;
import pt.it.av.atnog.csb.entity.csb.ServiceVendor;
import pt.it.av.atnog.csb.entity.paasmanager.PMApiLogin;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationCreateResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationDeleteResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationInfoResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationLogsResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationRestartResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationScaleResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStartResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStatisticsResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStatusResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStopResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMFramework;
import pt.it.av.atnog.csb.entity.paasmanager.PMMetric;
import pt.it.av.atnog.csb.entity.paasmanager.PMPaasCreate;
import pt.it.av.atnog.csb.entity.paasmanager.PMProviderCreateResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMPaasProvider;
import pt.it.av.atnog.csb.entity.paasmanager.PMPaasProviders;
import pt.it.av.atnog.csb.entity.paasmanager.PMRuntime;
import pt.it.av.atnog.csb.entity.paasmanager.PMService;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceCreateResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceDeleteResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceInfoResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceListInfoResponse;
import pt.it.av.atnog.csb.entity.ppm.PrivatePaas;
import pt.it.av.atnog.csb.paas.PaasProviderService;

/**
 * PT Inovacao PaaS Manager
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Stateless
@Path("/paas")
public class PaasProviderPTIn implements PaasProviderService {

	private PropertiesConfiguration propConfig;
	public static final String PAAS_MANAGER_PTIN_PROPERTIES_FILE_NAME = "pm-ptin.properties";
	
	@PersistenceContext(unitName = "CSBPU")
	private EntityManager em;

	private String pmServerUri;
	private String pmInfoGetPaasUri;
	private String pmAppsCreateAppUri;
	private String pmAppsDeployAppUri;
	private String pmAppsStartAppUri;
	private String pmAppsStopAppUri;
	private String pmAppsRestartAppUri;
	private String pmAppsDeleteAppUri;
	private String pmAppsScaleAppUri;
	private String pmAppsMigrateAppUri;
	private String pmInfoAppUri;
	private String pmInfoAppStatusUri;
	private String pmInfoAppStatistics;
	private String pmInfoAppLogsUri;
	private String pmCreateServiceUri;
	private String pmDeleteServiceUri;
	private String pmInfoServiceUri;
	private String pmListAppServicesUri;
	private String pmRegisterPaas;
	private String pmUpdatePaas;

	public PaasProviderPTIn() throws ConfigurationException {
		loadConfig();
	}
	
	public PaasProviderPTIn(EntityManager em) throws ConfigurationException {
		this();
		this.em = em;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaasProvider> getAllPaas() {

		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest request = cFactory.createRequest(pmInfoGetPaasUri);
		request.accept(MediaType.APPLICATION_XML);
		ClientResponse<PMPaasProviders> response;

		try {
			response = request.get(PMPaasProviders.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting the list of PaaS providers");
		}

		if (response.getStatus() != 200) {
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting the list of PaaS providers");
		}

		// convert PMPaasProviders to list of PaasProvider
		List<PMPaasProvider> pmpps = response.getEntity().getPaasProviders();
		List<PrivatePaas> privatePaasList = new PrivatePaasManager().offerings(em);
		List<PaasProvider> pps = new ArrayList<PaasProvider>();
		pps.addAll(convertToPaasProviders(pmpps));
		pps.addAll(privatePaasList);
		return pps;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationCreateResponse createApp(String appId, String provider, String framework) {
		String uri = getUriByQuery(pmAppsCreateAppUri, provider.toLowerCase(), appId, framework);
		try {
			return postUri(PMApplicationCreateResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating the app.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationCreateResponse deployApp(String appId, InputStream data) {
		try {
			// Delegate the deployment process to the PaaS Manager
			ClientRequestFactory cFactory = new ClientRequestFactory();
			ClientRequest cRequest = cFactory.createRequest(pmAppsDeployAppUri);

			MultipartFormDataOutput form = new MultipartFormDataOutput();
			form.addFormData("appID", appId, MediaType.TEXT_PLAIN_TYPE);
			form.addFormData("appData", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			cRequest.body(MediaType.MULTIPART_FORM_DATA_TYPE, form);
			cRequest.header("api-key", "csb"); // FIXME

			return cRequest.post(PMApplicationCreateResponse.class).getEntity();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationStartResponse startApp(String appId) {
		String uri = getUriByQuery(pmAppsStartAppUri, appId);
		try {
			return postUri(PMApplicationStartResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while starting the app.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationStopResponse stopApp(String appId) {
		String uri = getUriByQuery(pmAppsStopAppUri, appId);
		try {
			return postUri(PMApplicationStopResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while stopping the app.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationRestartResponse restartApp(String appId) {
		String uri = getUriByQuery(pmAppsRestartAppUri, appId);
		try {
			return postUri(PMApplicationRestartResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while restarting the app.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationDeleteResponse deleteApp(String appId) {
		String uri = getUriByQuery(pmAppsDeleteAppUri, appId);
		try {
			return deleteUri(PMApplicationDeleteResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting the app.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationStatusResponse statusApp(String appId) {
		String uri = getUriByQuery(pmInfoAppStatusUri, appId);
		try {
			return getUri(PMApplicationStatusResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting status of the app.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationScaleResponse scaleApp(String appId, int nInstances) {
		String uri = getUriByQuery(pmAppsScaleAppUri, appId, Integer.toString(nInstances));
		try {
			return postUri(PMApplicationScaleResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while scaling the app.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationInfoResponse infoApp(String appId) {
		String uri = getUriByQuery(pmInfoAppUri, appId);
		try {
			return getUri(PMApplicationInfoResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting app information.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationLogsResponse logsApp(String appId) {
		String uri = getUriByQuery(pmInfoAppLogsUri, appId);
		try {
			return getUri(PMApplicationLogsResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting app logs.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMServiceListInfoResponse getServices(String appId) {
		String uri = getUriByQuery(pmListAppServicesUri, appId);
		try {
			return getUri(PMServiceListInfoResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting service.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMServiceInfoResponse getService(String appId, String serviceId) {
		String uri = getUriByQuery(pmInfoServiceUri, appId, serviceId);
		try {
			return getUri(PMServiceInfoResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting service.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMServiceCreateResponse createService(String appId, String serviceId, String serviceVendorId) {
		String uri = getUriByQuery(pmCreateServiceUri, appId, serviceId, serviceVendorId);
		try {
			return postUri(PMServiceCreateResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while creating service.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMServiceDeleteResponse deleteService(String appId, String serviceId) {
		String uri = getUriByQuery(pmDeleteServiceUri, appId, serviceId);
		try {
			return deleteUri(PMServiceDeleteResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while deleting service.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PMApplicationStatisticsResponse statisticsApp(String appId, int samples) {
		String uri = getUriByQuery(pmInfoAppStatistics, appId, Integer.toString(samples));
		try {
			return getUri(PMApplicationStatisticsResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting app statistics.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public PMApplicationCreateResponse migrateApp(String appId, String providerId) {
		String uri = getUriByQuery(pmAppsMigrateAppUri, providerId.toLowerCase(), appId);
		try {
			return postUri(PMApplicationCreateResponse.class, uri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while migrating the app.");
		}
    }
	
	@Override
	public PMProviderCreateResponse registerPaas(String name, String type, String apiEndpoint, String apiKey, String apiSecret) throws Exception {
		System.out.println("api key: " + apiKey);
		System.out.println("api secret: " + apiSecret);
		
		PMApiLogin login = new PMApiLogin(apiKey, apiSecret);
		PMPaasCreate paas = new PMPaasCreate(name, type, apiEndpoint, login);
		
		JAXBContext context = JAXBContext.newInstance(PMPaasCreate.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(paas, System.out);
		
		
		context = JAXBContext.newInstance(PMApiLogin.class);
		m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(login, System.out);
		
		
		return postUri(PMProviderCreateResponse.class, pmRegisterPaas, paas);
	}
	
	@Override
	public PMProviderCreateResponse updatePaas(List<PaasProvider> providers) throws Exception {
		List<PMPaasProvider> paases = convertToPMPaasProviders(providers);
		PMPaasProviders ppp = new PMPaasProviders();
		ppp.setPaasProviders(paases);
		return putUri(PMProviderCreateResponse.class, pmUpdatePaas, ppp);
	}

	private static String getUriByQuery(String uri, String... args) {
		return MessageFormat.format(uri, (Object[]) args);
	}

	private static <T> T getUri(Class<T> returnType, String uri) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		cRequest.header("api-key", "csb");
		ClientResponse<T> cResponse = cRequest.get(returnType);
		return cResponse.getEntity();
	}

	private static <T> T postUri(Class<T> returnType, String uri) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		cRequest.header("api-key", "csb");
		ClientResponse<T> cResponse = cRequest.post(returnType);
		return cResponse.getEntity();
	}
	
	private static <T> T postUri(Class<T> returnType, String uri, Object obj) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		cRequest.body(MediaType.APPLICATION_XML_TYPE, obj);
		cRequest.header("api-key", "csb");
		ClientResponse<T> cResponse = cRequest.post(returnType);
		return cResponse.getEntity();
	}
	
	private static <T> T putUri(Class<T> returnType, String uri, Object obj) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		cRequest.body(MediaType.APPLICATION_XML_TYPE, obj);
		cRequest.header("api-key", "csb");
		ClientResponse<T> cResponse = cRequest.put(returnType);
		return cResponse.getEntity();
	}

	private static <T> T deleteUri(Class<T> returnType, String uri) throws Exception {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest cRequest = cFactory.createRequest(uri);
		cRequest.header("api-key", "csb");
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

	// private String getAppUrl(String appId) {
	// return appId + ".csb.atnog.av.it.pt"; // FIXME
	// }
	
	public static List<PMPaasProvider> convertToPMPaasProviders(List<PaasProvider> providers) {
		List<PMPaasProvider> pmproviders = new ArrayList<PMPaasProvider>();
		PMPaasProvider pmprovider;
		List<PMRuntime> runtimes;
		List<PMFramework> frameworks;
		List<PMMetric> metrics;
		List<PMService> services;
		
		for (PaasProvider p : providers) {
			pmprovider = new PMPaasProvider();
			runtimes = new ArrayList<PMRuntime>();
			frameworks = new ArrayList<PMFramework>();
			metrics = new ArrayList<PMMetric>();
			services = new ArrayList<PMService>();
			
			// id
			pmprovider.setName(p.getId());
			
			// runtimes
			if (p.getRuntimes() != null) {
				for (Runtime r : p.getRuntimes()) {
					runtimes.add(new PMRuntime(r.getId(), r.getName(), r.getVersion(), r.getInfo()));
				}
			}
			
			// frameworks
			if (p.getFrameworks() != null) {
				for (Framework f : p.getFrameworks()) {
					frameworks.add(new PMFramework(f.getId(), f.getName(), f.getVersion(), f.getInfo()));
				}
			}

			
			// services
			if (p.getServiceVendors() != null) {
				for (ServiceVendor s : p.getServiceVendors()) {
					services.add(new PMService(s.getId(), s.getName(), s.getVersion()));
				}
			}
			
			// metrics
			if (p.getMetrics() != null) {
				for (Metric m : p.getMetrics()) {
					if (m.getMetricEntries() != null) {
						for (MetricEntry me : m.getMetricEntries()) {
							metrics.add(new PMMetric(m.getName(), m.getInfo(), me.getValue(), m.getUnit()));
						}
					}
				}
			}
			
			pmprovider.setRuntimes(runtimes);
			pmprovider.setFrameworks(frameworks);
			pmprovider.setServices(services);
			pmprovider.setMetrics(metrics);
			pmproviders.add(pmprovider);
		}
		
		return pmproviders;
	}
	
	public static List<PaasProvider> convertToPaasProviders(List<PMPaasProvider> pmpps) {
		List<PaasProvider> pps = new ArrayList<PaasProvider>();
		PaasProvider pNew;
		List<Runtime> runtimes;
		List<Framework> frameworks;
		List<ServiceVendor> serviceVendors;
		List<Metric> metrics;
		Runtime r;
		Framework f;
		ServiceVendor sv;
		Metric m;

		for (PMPaasProvider pmp : pmpps) {
			pNew = new PaasProvider();

			// set name
			pNew.setId(pmp.getName());

			// convert list of PMRuntime to list of Runtime
			runtimes = new ArrayList<Runtime>();
			if (pmp.getRuntimes() != null) {
				for (PMRuntime pmr : pmp.getRuntimes()) {
					r = new Runtime(pmr.getId(), pmr.getName(), pmr.getVersion());
					runtimes.add(r);
				}
			}

			pNew.setRuntimes(runtimes);

			// convert list of PMFramewwork to list of Framework
			frameworks = new ArrayList<Framework>();
			if (pmp.getFrameworks() != null) {
				for (PMFramework pmf : pmp.getFrameworks()) {
					f = new Framework(pmf.getId(), pmf.getName(), pmf.getVersion());
					frameworks.add(f);
				}
			}

			pNew.setFrameworks(frameworks);

			// convert list of PMService to list of Service
			serviceVendors = new ArrayList<ServiceVendor>();

			if (pmp.getServices() != null) {
				for (PMService pms : pmp.getServices()) {
					sv = new ServiceVendor(pms.getId(), pms.getName(), pms.getVersion());
					serviceVendors.add(sv);
				}
			}

			pNew.setServiceVendors(serviceVendors);

			// convert list of PMMetric to list of Metric
			metrics = new ArrayList<Metric>();
			if (pmp.getMetrics() != null) {
				for (PMMetric pmm : pmp.getMetrics()) {
					m = new Metric();
					m.setName(pmm.getName());
					metrics.add(m);
				}
			}

			pNew.setMetrics(metrics);
			pps.add(pNew);
		}
		
		return pps;
	}

	private void loadConfig() throws ConfigurationException {
		propConfig = new PropertiesConfiguration();
		InputStream inputStream = this.getClass().getClassLoader()
		        .getResourceAsStream(PAAS_MANAGER_PTIN_PROPERTIES_FILE_NAME);
		propConfig.load(inputStream);

		pmServerUri = propConfig.getString("pm_server_uri");

		pmAppsCreateAppUri = pmServerUri + propConfig.getString("pm_apps_createapp_uri");
		pmAppsDeployAppUri = pmServerUri + propConfig.getString("pm_apps_deployapp_uri");
		pmAppsStartAppUri = pmServerUri + propConfig.getString("pm_apps_startapp_uri");
		pmAppsStopAppUri = pmServerUri + propConfig.getString("pm_apps_stopapp_uri");
		pmAppsRestartAppUri = pmServerUri + propConfig.getString("pm_apps_restartapp_uri");
		pmAppsDeleteAppUri = pmServerUri + propConfig.getString("pm_apps_deleteapp_uri");
		pmAppsScaleAppUri = pmServerUri + propConfig.getString("pm_apps_scaleapp_uri");
		pmAppsMigrateAppUri = pmServerUri + propConfig.getString("pm_apps_migrateapp_uri");
		pmInfoAppStatusUri = pmServerUri + propConfig.getString("pm_info_getappstatus_uri");
		pmInfoAppUri = pmServerUri + propConfig.getString("pm_info_getappinfo_uri");
		pmInfoAppStatistics = pmServerUri + propConfig.getString("pm_info_app_statistics_uri");
		pmInfoAppLogsUri = pmServerUri + propConfig.getString("pm_info_getapplogs_uri");
		pmInfoGetPaasUri = pmServerUri + propConfig.getString("pm_info_getpaas_uri");

		pmCreateServiceUri = pmServerUri + propConfig.getString("pm_service_create_uri");
		pmDeleteServiceUri = pmServerUri + propConfig.getString("pm_service_delete_uri");
		pmInfoServiceUri = pmServerUri + propConfig.getString("pm_service_info_uri");
		pmListAppServicesUri = pmServerUri + propConfig.getString("pm_services_info_app_uri");
		
		pmRegisterPaas = pmServerUri + propConfig.getString("pm_register_paas_uri");
		pmUpdatePaas = pmServerUri + propConfig.getString("pm_update_paas_uri");
	}

}
