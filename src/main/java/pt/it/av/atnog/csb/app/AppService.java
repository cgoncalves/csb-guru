package pt.it.av.atnog.csb.app;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pt.it.av.atnog.csb.entity.common.ApplicationCreateResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationDeleteResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationDeployResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationInfoResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationRestartResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationScaleResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStartResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStatusResponse;
import pt.it.av.atnog.csb.entity.common.ApplicationStopResponse;
import pt.it.av.atnog.csb.entity.common.ServiceCreateResponse;
import pt.it.av.atnog.csb.entity.common.ServiceDeleteResponse;
import pt.it.av.atnog.csb.entity.csb.ACMLog;
import pt.it.av.atnog.csb.entity.csb.App;
import pt.it.av.atnog.csb.entity.csb.Apps;
import pt.it.av.atnog.csb.entity.csb.Manifest;

/**
 * REST Web Service
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */

@Path("/apps")
public interface AppService {

	@Path("/")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Apps getApps();

	@Path("/{appId}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public App getApp(@PathParam("appId") String appId);

	@Path("/{appId}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationCreateResponse createApp(@PathParam("appId") String appId, Manifest manifest);

	@Path("/{appId}/deploy")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationDeployResponse deployApp(@PathParam("appId") String appId);

	@Path("/{appId}/start")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationStartResponse startApp(@PathParam("appId") String appId);

	@Path("/{appId}/stop")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationStopResponse stopApp(@PathParam("appId") String appId);

	@Path("/{appId}/restart")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationRestartResponse restartApp(@PathParam("appId") String appId);

	@Path("/{appId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationDeleteResponse deleteApp(@PathParam("appId") String appId);

	@Path("/{appId}/status")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationStatusResponse statusApp(@PathParam("appId") String appId);

	@Path("/{appId}/log")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ACMLog logApp(@PathParam("appId") String appId);

	@Path("/{appId}/info")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationInfoResponse infoApp(@PathParam("appId") String appId);

	@Path("/{appId}/scale/{nInstances}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ApplicationScaleResponse scaleApp(@PathParam("appId") String appId, @PathParam("nInstances") int nInstances);

	@Path("/{appId}/services/{serviceId}/{csbServiceName}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ServiceCreateResponse createService(@PathParam("appId") String appId,
	        @PathParam("serviceId") String serviceId, @PathParam("csbServiceName") String serviceName);

	@Path("{appId}/services/{csbServiceName}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ServiceDeleteResponse deleteService(@PathParam("appId") String appId,
	        @PathParam("csbServiceName") String serviceName);
}
