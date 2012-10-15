package pt.it.av.atnog.csb.manifest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;
import pt.it.av.atnog.csb.paas.PaasProviderService;
import pt.it.av.atnog.csb.paas.manager.PaasProviderPTIn;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Stateless
@Path("/manifest")
public class ManifestServiceImpl implements ManifestService {

	@Override
	public List<PaasProvider> postManifest(Manifest manifest) throws Exception {
		RuleEngineRunner re = RuleEngineRunner.getInstance();
		re.setRules(manifest.getRules());
		re.setInitialProvidersIn(new PaasProviderPTIn().getAllPaas());

		List<String> out = re.run();
		List<PaasProvider> providers = new ArrayList<PaasProvider>();
		PaasProviderService pps = new PaasProviderPTIn();
		List<PaasProvider> pp = pps.getAllPaas();
		for (String name : out) {
			for (PaasProvider p : pp) {
				if (p.getId().equals(name.toUpperCase())) {
					providers.add(p);
				}
			}
		}

		return providers;
	}
}
