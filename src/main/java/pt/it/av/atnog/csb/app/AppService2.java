package pt.it.av.atnog.csb.app;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

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

public interface AppService2 {
	
	@Path("/{appId}/cenas")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<App> getAppsAsync();

	/**
	 * Get all apps.
	 * 
	 * @return all apps
	 */
	@Path("/")
	@GET
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Wrapped(element = "apps")
	public List<App> getApps(@Context SecurityContext ctx);

	/**
	 * Get an {@link App} given an appId
	 * 
	 * @param appId
	 *            the application ID
	 * @return an {@link App}
	 */
	@Path("/{appId}")
	@GET
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public App getApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

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
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createApp(@Context SecurityContext ctx, @PathParam("appId") String appId, @FormParam("repository_type") @DefaultValue("git")  String repositoryType);
	
	@Path("/{appId}/manifest")
	@POST
	@RolesAllowed( {"user",  "admin"} )
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createManifest(@Context SecurityContext ctx, @PathParam("appId") String appId, Manifest manifest);

	/**
	 * Trigger the deployment process of an {@link App}.
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/deploy")
	@PUT
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deployApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

	/**
	 * Start an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/start")
	@PUT
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response startApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

	/**
	 * Stop an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/stop")
	@PUT
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response stopApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

	/**
	 * Restart an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/restart")
	@PUT
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response restartApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

	/**
	 * Delete an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}")
	@DELETE
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

	/**
	 * Get the status of an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/status")
	@GET
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationStatusResponse statusApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

//	/**
//	 * Get the commit log an {@link App}
//	 * 
//	 * @param appId
//	 *            the {@link App} ID
//	 * @return
//	 */
//	@Path("/{appId}/commitlog")
//	@GET
//	@RolesAllowed( {"user",  "admin"} )
//	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public ACMLog commitLogApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

	/**
	 * Get info about an {@link App}
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/info")
	@GET
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PMApplicationInfoResponse infoApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

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
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response scaleApp(@Context SecurityContext ctx, @PathParam("appId") String appId, @PathParam("nInstances") int nInstances);
	
	@Path("/{appId}/migrate/{providerId}")
	@PUT
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public App migrateApp(@Context SecurityContext ctx, @PathParam("appId") String appId, @PathParam("providerId") String providerId);

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
	@Path("/{appId}/services/{serviceId}")
	@POST
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createService(@Context SecurityContext ctx, @PathParam("appId") String appId, @PathParam("serviceId") String serviceId,
	        @QueryParam("vendorId") String vendorId);

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
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteService(@Context SecurityContext ctx, @PathParam("appId") String appId, @PathParam("csbServiceId") String csbServiceId);

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
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Service getService(@Context SecurityContext ctx, @PathParam("appId") String appId, @PathParam("csbServiceId") String csbServiceId);

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
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Wrapped(element = "services")
	public List<Service> getServices(@Context SecurityContext ctx, @PathParam("appId") String appId);

	/**
	 * Get {@link App} logs
	 * 
	 * @param appId
	 *            the {@link App} ID
	 * @return
	 */
	@Path("/{appId}/log")
	@GET
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Log logApp(@Context SecurityContext ctx, @PathParam("appId") String appId);

	@Path("/{appId}/monitor")
	@GET
	@RolesAllowed( {"user",  "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Monitor monitorApp(@Context SecurityContext ctx, @PathParam("appId") String appId, @QueryParam("samples") int samples);
}
