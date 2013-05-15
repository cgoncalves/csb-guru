package pt.it.av.atnog.csb.paas;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

import pt.it.av.atnog.csb.entity.csb.PaasProvider;
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
import pt.it.av.atnog.csb.entity.paasmanager.PMProviderCreateResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceCreateResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceDeleteResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceInfoResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMServiceListInfoResponse;

/**
 * REST Web PMService
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */

public interface PaasProviderService {

	@Path("/offerings")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Wrapped(element = "paas_providers")
	public List<PaasProvider> getAllPaas();

	@Path("/{appId}/{provider}/{framework}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationCreateResponse createApp(@PathParam("appId") String appId,
	        @PathParam("provider") String provider, @PathParam("framework") String framework);

	// @Path("/{appId}/deploy")
	// @PUT
	// @Consumes({ MediaType.MULTIPART_FORM_DATA })
	// @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public PMApplicationDeployResponse deployApp(@PathParam("appId") String
	// appId, @MultipartForm DataDeployAppForm uploadForm);
	// public PMApplicationDeployResponse deployApp(@PathParam("appId") String
	// appId);
	public PMApplicationCreateResponse deployApp(String appId, InputStream data);

	@Path("/{appId}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationInfoResponse infoApp(@PathParam("appId") String appId);

	@Path("/{appId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationDeleteResponse deleteApp(@PathParam("appId") String appId);

	@Path("/{appId}/start")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationStartResponse startApp(@PathParam("appId") String appId);

	@Path("/{appId}/stop")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationStopResponse stopApp(@PathParam("appId") String appId);

	@Path("/{appId}/restart")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationRestartResponse restartApp(@PathParam("appId") String appId);

	@Path("/{appId}/status")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationStatusResponse statusApp(@PathParam("appId") String appId);

	@Path("/{appId}/scale/{nInstances}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationScaleResponse scaleApp(@PathParam("appId") String appId, @PathParam("nInstances") int nInstances);

	@Path("/{appId}/logs")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationLogsResponse logsApp(@PathParam("appId") String appId);

	@Path("/{appId}/statistics")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationStatisticsResponse statisticsApp(@PathParam("appId") String appId, @QueryParam("period") int samples);
	
	@Path("/{appId}/migrate/{providerId}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationCreateResponse migrateApp(@PathParam("appId") String appId, @PathParam("providerId") String providerId);

	@Path("/{appId}/services")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMServiceListInfoResponse getServices(@PathParam("appId") String appId);
	
	@Path("/{appId}/service/{serviceId}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMServiceInfoResponse getService(@PathParam("appId") String appId, @PathParam("serviceId") String serviceId);
	
	@Path("/{appId}/services/{serviceId}/{serviceVendorId}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMServiceCreateResponse createService(@PathParam("appId") String appId,
	        @PathParam("serviceId") String serviceId, @PathParam("serviceVendorId") String serviceVendorId);

	@Path("/{appId}/services/{serviceId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMServiceDeleteResponse deleteService(@PathParam("appId") String appId,
	        @PathParam("serviceId") String serviceId);
	
	public PMProviderCreateResponse registerPaas(String name, String type, String apiEndpoint, String apiKey, String apiSecret) throws Exception;
	
	public PMProviderCreateResponse updatePaas(List<PaasProvider> providers) throws Exception;
}
