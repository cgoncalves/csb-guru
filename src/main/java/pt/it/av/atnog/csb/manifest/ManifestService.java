/**
 * 
 */
package pt.it.av.atnog.csb.manifest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pt.it.av.atnog.csb.entity.common.PaasProviders;
import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.ManifestResponse;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */

@Path("/manifest")
public interface ManifestService {

	/**
	 * Evaluate a {@link Manifest}.
	 * 
	 * @param manifest the {@link Manifest} to evaluate
	 * @return a list of rated {@link PaasProviders}
	 */
	@Path("/")
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ManifestResponse postManifest(Manifest manifest);
}
