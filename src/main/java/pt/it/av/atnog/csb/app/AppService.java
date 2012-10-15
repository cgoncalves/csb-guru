package pt.it.av.atnog.csb.app;

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
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

import pt.it.av.atnog.csb.entity.csb.ACMLog;
import pt.it.av.atnog.csb.entity.csb.App;
import pt.it.av.atnog.csb.entity.csb.Log;
import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.Monitor;
import pt.it.av.atnog.csb.entity.csb.Service;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationInfoResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMApplicationStatusResponse;
import pt.it.av.atnog.csb.entity.paasmanager.PMService;

/**
 * REST Web PMService
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */

public interface AppService {

	/**
	 * Get all apps.
	 * 
	 * @return all apps
	 */
	@Path("/")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Wrapped(element = "apps")
	public List<App> getApps();

	/**
	 * Get an {@link App} given an appId
	 * 
	 * @param appId
	 *            the application ID
	 * @return an {@link App}
	 */
	@Path("/{appId}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public App getApp(@PathParam("appId") String appId);

	/**
	 * Create a new {@link App}.
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @param manifest
	 *            the {@link Manifest}
	 */
	@Path("/{appId}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createApp(@PathParam("appId") String appId, Manifest manifest);

	/**
	 * Trigger the deployment process of an {@link App}.
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/deploy")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deployApp(@PathParam("appId") String appId);

	/**
	 * Start an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/start")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response startApp(@PathParam("appId") String appId);

	/**
	 * Stop an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/stop")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response stopApp(@PathParam("appId") String appId);

	/**
	 * Restart an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/restart")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response restartApp(@PathParam("appId") String appId);

	/**
	 * Delete an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteApp(@PathParam("appId") String appId);

	/**
	 * Get the status of an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/status")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationStatusResponse statusApp(@PathParam("appId") String appId);

	/**
	 * Get the commit log an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/commitlog")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ACMLog commitLogApp(@PathParam("appId") String appId);

	/**
	 * Get info about an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/info")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationInfoResponse infoApp(@PathParam("appId") String appId);

	/**
	 * Scale an {@link App} to <code>nInstances</code>
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @param nInstances
	 * @return
	 */
	@Path("/{appId}/scale/{nInstances}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response scaleApp(@PathParam("appId") String appId, @PathParam("nInstances") int nInstances);
	
	@Path("/{appId}/migrate/{providerId}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public App migrateApp(@PathParam("appId") String appId, @PathParam("providerId") String providerId);

	/**
	 * Create a new service for an {@link App}.
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @param serviceId
	 *            the {@link PMService} ID
	 * @param csbServiceId
	 * @return
	 */
	@Path("/{appId}/services/{serviceId}/{vendorId}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createService(@PathParam("appId") String appId, @PathParam("serviceId") String serviceId,
	        @PathParam("vendorId") String vendorId);

	/**
	 * Delete a service associated to an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @param csbServiceId
	 * @return
	 */
	@Path("{appId}/services/{csbServiceId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteService(@PathParam("appId") String appId, @PathParam("csbServiceId") String csbServiceId);

	/**
	 * Get a service associated to an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @param csbServiceId
	 * @return
	 */
	@Path("{appId}/services/{csbServiceId}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Service getService(@PathParam("appId") String appId, @PathParam("csbServiceId") String csbServiceId);

	/**
	 * Get a list of services associated to an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @param csbServiceId
	 * @return
	 */
	@Path("{appId}/services")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Wrapped(element = "services")
	public List<Service> getServices(@PathParam("appId") String appId);

	/**
	 * Get {@link App} logs
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/log")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Log logApp(@PathParam("appId") String appId);

	@Path("/{appId}/monitor")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Monitor monitorApp(@PathParam("appId") String appId, @QueryParam("samples") int samples);
}
