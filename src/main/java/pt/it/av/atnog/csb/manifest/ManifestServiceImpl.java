package pt.it.av.atnog.csb.manifest;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.configuration.ConfigurationException;

import pt.it.av.atnog.csb.entity.common.PaasProvider;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
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
}
