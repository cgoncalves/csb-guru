package pt.it.av.atnog.csb.user;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import pt.it.av.atnog.csb.entity.csb.User;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Path("/users")
public interface UserResource {

	@Path("/")
	@POST
	@RolesAllowed("admin")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User createUser(@FormParam("name") String name, @FormParam("password") String password, @FormParam("email") String email);

	@Path("/{id}")
	@DELETE
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void deleteUser(@PathParam("id") String id);
	
	@Path("/")
	@GET
	@RolesAllowed( {"user", "admin"} )
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUser(@Context SecurityContext ctx);
	
	@Path("/{name}")
	@GET
	@PermitAll
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUser(@Context SecurityContext ctx, @PathParam("name") String name);
}
