package pt.it.av.atnog.csb.manifest;

import java.util.ArrayList;
import java.util.List;

import pt.it.av.atnog.csb.entity.common.Framework;
import pt.it.av.atnog.csb.entity.common.PaasProvider;
import pt.it.av.atnog.csb.entity.common.Runtime;
import pt.it.av.atnog.csb.entity.common.Service;
import pt.it.av.atnog.csb.entity.csb.Manifest;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public class ScoreTable {
	
	private final List<PaasProvider> providers;
	private int numRequirements;

	protected ScoreTable(final List<PaasProvider> providers) {
		this.numRequirements = 0;
		this.providers = providers;

		for (PaasProvider p : this.providers) {
			// set initial score to 0
			p.setScore(0);
		}
	}

	public List<PaasProvider> score(final Manifest manifest) {
		numRequirements = 2 + manifest.getServices().size(); // 1xruntime + 1xframework + services
		
		Runtime runtime = manifest.getRuntime();
		Framework framework = manifest.getFramework();
		List<Service> services = manifest.getServices();
		
		List<Runtime> runtimesToRemove = new ArrayList<Runtime>();
		List<Framework> frameworksToRemove = new ArrayList<Framework>();
		List<Service> servicesToRemove = new ArrayList<Service>();
		
		boolean foundService;

		for (PaasProvider p : providers) {
			// score runtimes
			for (Runtime r : p.getRuntimes()) {
				if (runtime.getId().equals(r.getId())) {
					// found a match, increment score
					p.setScore(p.getScore() + 1);
					break;
				}
				else
					runtimesToRemove.add(r);
			}

			// score frameworks
			for (Framework f : p.getFrameworks()) {
				if (framework.getId().equals(f.getId())) {
					// found a match, increment score
					p.setScore(p.getScore() + 1);
					break;
				}
				else
					frameworksToRemove.add(f);
			}

			for (Service sProvider : p.getServices()) {
				foundService = false;
				for (Service sManifest : services) {
					if (sManifest.getId().equals(sProvider.getId())) {
						// found a match, increment score
						p.setScore(p.getScore() + 1);
						foundService = true;
						break;
					}
				}
				
				if (!foundService)
					servicesToRemove.add(sProvider);
			}
			
			p.getRuntimes().removeAll(runtimesToRemove);
			p.getFrameworks().removeAll(frameworksToRemove);
			p.getServices().removeAll(servicesToRemove);
			
			runtimesToRemove.clear();
			frameworksToRemove.clear();
			servicesToRemove.clear();
		}

		return providers;
	}

	public final List<PaasProvider> scoreInPercentage(final Manifest manifest) {

		List<PaasProvider> providers = score(manifest);

		int scoreNormalized;

		for (PaasProvider p : providers) {
			scoreNormalized = (int) ((double) p.getScore() / (double) numRequirements * 100);
			p.setScore(scoreNormalized);
		}

		return providers;
	}

	/**
	 * @return the providers
	 */
	public final List<PaasProvider> getProviders() {
		return providers;
	}

}
