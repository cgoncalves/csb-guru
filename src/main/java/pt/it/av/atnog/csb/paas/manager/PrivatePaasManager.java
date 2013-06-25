package pt.it.av.atnog.csb.paas.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.it.av.atnog.csb.acm.ACM2;
import pt.it.av.atnog.csb.acm.ACM2Impl;
import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.csb.App;
import pt.it.av.atnog.csb.entity.csb.Framework;
import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.entity.csb.Metric;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;
import pt.it.av.atnog.csb.entity.csb.App.DeployState;
import pt.it.av.atnog.csb.entity.csb.Runtime;
import pt.it.av.atnog.csb.entity.csb.ServiceVendor;
import pt.it.av.atnog.csb.entity.ppm.PrivatePaas;
import pt.it.av.atnog.csb.entity.ppm.PrivatePaas.State;
import pt.it.av.atnog.csb.paas.PaasProviderService;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Stateless
@Path("/ppm")
public class PrivatePaasManager {
	
	private static final Logger logger = LoggerFactory.getLogger(PrivatePaasManager.class);
	
	@PersistenceContext(unitName = "CSBPU")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<PrivatePaas> offerings(EntityManager em) {
		Query q = em.createQuery("SELECT p FROM PrivatePaas p");
		List<PrivatePaas> providers = q.getResultList();

		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(PaasProvider.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			InputStream is = PrivatePaasManager.class.getResourceAsStream("/private_paas.xml");
			PaasProvider template = (PaasProvider) unmarshaller.unmarshal(is);
			
			for(PaasProvider p : providers) {
				logger.debug("Found private PaaS {}", p.getId());
				p.setFrameworks(template.getFrameworks());
				p.setMetrics(template.getMetrics());
				p.setRuntimes(template.getRuntimes());
				p.setServiceVendors(template.getServiceVendors());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return providers;
	}

	public List<PrivatePaas> offeringsUnbootstrapped(EntityManager em) {
		return em.createQuery("SELECT p FROM PrivatePaas p WHERE p.state=0", PrivatePaas.class).getResultList();
	}
	
	@Path("/register")
	@POST
	public Response registerPaas(@FormParam("id") String id, @FormParam("endpoint") String endpoint, @FormParam("key") String key, @FormParam("secret") String secret) throws ConfigurationException {
		logger.debug("Registering new PaaS with id {}", id);
		
		// set state to bootstrapped
		PrivatePaas pp = em.find(PrivatePaas.class, id);
		pp.setState(State.BOOTSTRAPPED);
		em.persist(pp);
		em.flush();
		
		PaasProvider pn = null;

		PaasProviderPTIn ppp = new PaasProviderPTIn(em);
		for (PaasProvider p : ppp.getAllPaas()) {
			if (p.getId().equals(id)) {
				pn = p;
				break;
			}
		}
		
		if (pn == null) {
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Could not find a Private PaaS with ID" + id);
		}
		
		// register PaaS on PP and inform PP about its features
		try {
			PaasProviderService pps = new PaasProviderPTIn();
			List<PaasProvider> providers = new ArrayList<PaasProvider>();
			providers.add(pn);
			
			System.err.println("-----------------------------------NOVO PAAS-----------------------------------------");
			for (PaasProvider p : providers) {
				if (p.getRuntimes() != null) {
					System.err.println("Runtimes");
					for (Runtime r : p.getRuntimes()) {
						System.err.println(r.getId());
					}
				}
				if (p.getFrameworks() != null) {
					System.err.println("Frameworks");
					for (Framework f : p.getFrameworks()) {
						System.err.println(f.getId());
					}
				}
				if (p.getServiceVendors() != null) {
					System.err.println("Services");
					for (ServiceVendor r : p.getServiceVendors()) {
						System.err.println(r.getId());
					}
				}
				if (p.getMetrics() != null) {
					System.err.println("Metrics");
					for (Metric r : p.getMetrics()) {
						System.err.println(r.getName() + " " + r.getUnit());
					}
				}
			}
			System.err.println("-----------------------------------FIM NOVO PAAS-----------------------------------------");
			pps.registerPaas(id, "private", endpoint, key, secret);
			pps.updatePaas(providers);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while registering new PaaS.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while registering new PaaS.");
		}
		
		// check for pending app deployments
		List<App> pendingApps = em.createNamedQuery("App.findAllDeployPending", App.class).getResultList();
		
		ACM2 acm2 = ACM2Impl.getInstance();
		String appUrl;
		
		try {
			PaasProviderService pps = new PaasProviderPTIn(em);
			
			if (pendingApps != null) {
				for (App app : pendingApps) {
					Manifest manifest = acm2.getManifest(acm2.getRepository(app.getId(), app.getRepositoryType()));

					if (manifest.getProvider().equals(id)) {
						app.setDeployState(DeployState.DEPLOYING);
						em.persist(app);	
						
						if (app.getProvider() == null) {
							// app is not yet created in the PaaS Manager. create it first!
							pps.createApp(app.getId(), manifest.getProvider(), manifest.getFramework());
						}
	
						// deploy!
						logger.debug("Deploying app {} to PaaS {}", app.getId(), manifest.getProvider());
						File zipFile = acm2.zipRepository(acm2.getRepository(app.getId(), app.getRepositoryType()));
						try {
							InputStream is = new FileInputStream(zipFile);
							appUrl = pps.deployApp(app.getId(), is).getAppUrl();
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
							throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Error with intput stream");
						} finally {
							zipFile.delete();								
						}
						logger.debug("Deployed app {} to PaaS {}", app.getId(), manifest.getProvider());
			
						// by this time the app was successfully created and deployed
	//					provider = em.find(PaasProvider.class, cResponse.getPaasProvider());
	//					if (provider == null) {
	//						// provider doesn't exist yet in database. insert it!
	//						provider = new PaasProvider();
	//						provider.setId(cResponse.getPaasProvider());
	//						em.persist(provider);
	//					}
			
						// set app's provider
						app.setProvider(pp);
						app.setUrl(appUrl);
						app.setDeployState(DeployState.DEPLOYED);
						em.persist(app);
					}
				}
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/cenas1")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<PrivatePaas> getCenas1() {
		return offerings(em);
	}
	
	@GET
	@Path("/cenas2")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<PaasProvider> getCenas2() throws ConfigurationException {
		PaasProviderPTIn ppp = new PaasProviderPTIn(em);
		return ppp.getAllPaas();
	}
	
}
