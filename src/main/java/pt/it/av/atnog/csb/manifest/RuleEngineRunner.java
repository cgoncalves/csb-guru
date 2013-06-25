package pt.it.av.atnog.csb.manifest;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import jpl.Atom;
import jpl.JPL;
import jpl.Query;
import jpl.Term;
import jpl.Util;
import jpl.Variable;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;
import pt.it.av.atnog.csb.entity.csb.Rule;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
public class RuleEngineRunner {

	private List<Rule> rules;
	private List<String> initialProvidersIn;
	private static RuleEngineRunner INSTANCE = null;
	private static String RULES_FILE = "/home/ubuntu/csb2.pro";

	private RuleEngineRunner() throws FileNotFoundException {
		init();
	}

	public static RuleEngineRunner getInstance() {
		if (INSTANCE == null) {
			try {
				INSTANCE = new RuleEngineRunner();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return INSTANCE;
		} else {
			return INSTANCE;
		}
	}

	/**
	 * Connects to the engine and injects the rules file into it. This is done
	 * once during the lifetime of the engine.
	 * 
	 * @throws FileNotFoundException
	 *             if file RULES_FILE is not found.
	 */
	private void init() throws FileNotFoundException {
		System.err.println("A INICIALIZAR JPL...");
		JPL.init();
		System.err.println("JPL INICIALIZADO!");
		Query consultQuery = new Query("consult", new Term[] { new Atom(RULES_FILE) });
		if (!consultQuery.hasSolution()) {
			throw new FileNotFoundException("File not found: " + RULES_FILE);
		}
		consultQuery.close();
	}

	/**
	 * Stops the prolog engine.
	 */
	private void destroy() {
		Query haltQuery = new Query("halt");
		haltQuery.hasSolution();
		haltQuery.close();
	}

	public List<String> run() throws Exception {

		if (initialProvidersIn == null || initialProvidersIn.isEmpty()) {
			throw new Exception("Initial providers list is uninitialized or empty");
		}
		

		Query q;
		Term[] terms;
		Term providerList = Util.textToTerm(this.initialProvidersIn.toString());

		for (Rule rule : rules) {
			// size = 1x providers list in + 1x providers list out + Nx remaining terms
			terms = new Term[2 + rule.getParams().size()];
			terms[0] = providerList;
			terms[1] = new Variable("PLOut");

			for (int i = 0; i < rule.getParams().size(); i++) {
				terms[i + 2] = Util.textToTerm(rule.getParams().get(i).toLowerCase());
			}

			q = new Query("rule_" + rule.getName(), terms);
			q.close();
			providerList = (Term) q.oneSolution().get("PLOut");
			System.out.println(q + " processed:\n\t" + providerList.toString());
			if (providerList.listLength() == 0) {
				System.out.println("Providers list out is empty. Aborting!");
				break;
			}
		}

		List<String> list = new ArrayList<String>();
		for (Term term : providerList.toTermArray()) {
			list.add(term.toString());
		}

		return list;
	}

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * @param rules
	 *            the rules to set
	 */
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * @return the initialProvidersIn
	 */
	public final List<String> getInitialProvidersIn() {
		return initialProvidersIn;
	}

	/**
	 * @param initialProvidersIn
	 *            the initialProvidersIn to set
	 */
	public final void setInitialProvidersIn(List<PaasProvider> initialProvidersIn) {
		this.initialProvidersIn = new ArrayList<String>();

		for (PaasProvider p : initialProvidersIn) {
			this.initialProvidersIn.add(p.getId().toLowerCase());
		}
	}

//	private static List<PaasProvider> getProviders() {
//		ClientRequestFactory cFactory = new ClientRequestFactory();
//		ClientRequest request = cFactory.createRequest("http://fog.av.it.pt:8080/paasmanager/v1/info/paas/offering");
//		request.accept(MediaType.APPLICATION_XML);
//		ClientResponse<PaasProviders> response;
//
//		try {
//			response = request.get(PaasProviders.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
//			        "Internal server error while getting the list of PaaS providers");
//		}
//
//		if (response.getStatus() != 200) {
//			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
//			        "Internal server error while getting the list of PaaS providers");
//		}
//
//		return response.getEntity().getPaasProviders();
//	}
}
