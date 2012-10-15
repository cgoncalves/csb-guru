package pt.it.av.atnog.csb.manifest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
public interface ManifestService {

	@Path("/")
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Wrapped(element = "providers")
	public List<PaasProvider> postManifest(Manifest manifest) throws Exception;
}
