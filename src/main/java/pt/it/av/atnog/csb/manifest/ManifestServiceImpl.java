package pt.it.av.atnog.csb.manifest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.configuration.ConfigurationException;

import pt.it.av.atnog.csb.entity.common.Framework;
import pt.it.av.atnog.csb.entity.common.PaasProvider;
import pt.it.av.atnog.csb.entity.common.Runtime;
import pt.it.av.atnog.csb.entity.common.Service;
import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.ManifestResponse;
import pt.it.av.atnog.csb.exception.CSBException;
import pt.it.av.atnog.csb.paas.PaasProviderService;
import pt.it.av.atnog.csb.paas.manager.PaasProviderPTIn;

/**
 * REST Web Service
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */

public class ManifestServiceImpl implements ManifestService {

	public ManifestResponse postManifest(Manifest manifest) {

		ManifestResponse response = new ManifestResponse();

		PaasProviderService paasService;
		List<PaasProvider> providers;

		try {
			paasService = new PaasProviderPTIn();
			providers = paasService.getAllPaas().getPaasProviders();

			// score PaaS providers
			ScoreTable scoreTable = new ScoreTable(providers);
			List<PaasProvider> providersScored = scoreTable.scoreInPercentage(manifest);

			response.setPaasProviders(providersScored);

		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "#TODO"); // TODO
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "#TODO"); // TODO
		}

		return response;
	}

	@Override
    public Manifest toJson() {
		Runtime r1 = new Runtime("r1ID", "name", "version", "info");
		Framework f1 = new Framework("f1ID", "name", "version", "info");
		Service s1 = new Service("s1ID", "name", "version", "info");
		Service s2 = new Service("s2ID", "name", "version", "info");
		Service s3 = new Service("s3ID", "name", "version", "info");
		
		Manifest manifest = new Manifest();
		
		List<Service> ss = new ArrayList<Service>();
		ss.add(s1);
		ss.add(s2);
		ss.add(s3);
		
		manifest.setRuntime(r1);
		manifest.setFramework(f1);
		manifest.setServices(ss);
		
	    return manifest;
    }
}
