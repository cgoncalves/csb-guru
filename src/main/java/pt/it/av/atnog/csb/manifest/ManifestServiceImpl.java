package pt.it.av.atnog.csb.manifest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;

import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;
import pt.it.av.atnog.csb.entity.csb.Rule;
import pt.it.av.atnog.csb.paas.PaasProviderService;
import pt.it.av.atnog.csb.paas.manager.PaasProviderPTIn;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Stateless
@Path("/manifest")
public class ManifestServiceImpl implements ManifestService {

	@PersistenceContext(unitName = "CSBPU")
	private EntityManager em;

	@Override
	public List<PaasProvider> postManifest(Manifest manifest) throws Exception {
		PaasProviderService pps = new PaasProviderPTIn(em);
		List<PaasProvider> pp = pps.getAllPaas();

		RuleEngineRunner re = RuleEngineRunner.getInstance();
		re.setRules(manifest.getRules());
		re.setInitialProvidersIn(pp);

		List<String> out = re.run();
		List<PaasProvider> providers = new ArrayList<PaasProvider>();
		for (String name : out) {
			for (PaasProvider p : pp) {
				if (p.getId().equals(name.toUpperCase())) {
//					p.setFrameworks(null);
//					p.setMetrics(null);
//					p.setRuntimes(null);
//					p.setServiceVendors(null);
					providers.add(p);
				}
			}
		}

		return providers;
	}

	@Override
	public Manifest getManifest() {
		Manifest manifest = new Manifest();
		manifest.setProvider("Heroku");
		List<Rule> rules = new ArrayList<Rule>();
		rules.add(new Rule("runtime", "JAVA_1_6"));
		rules.add(new Rule("framework", "GRAILS"));
		rules.add(new Rule("service", "MONGODB_1_8"));
//		rules.add(new Rule("service", "postgresql", "greater", "9.0"));
		rules.add(new Rule("metric", "usage_cpu"));
		rules.add(new Rule("metric", "memory"));
		rules.add(new Rule("metric", "disk"));
		manifest.setRules(rules);
		return manifest;
	}

	@Override
	public List<PaasProvider> getManifestResults() throws Exception {
		return postManifest(getManifest());
	}
}
