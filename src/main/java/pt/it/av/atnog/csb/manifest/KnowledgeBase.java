package pt.it.av.atnog.csb.manifest;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;

import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.csb.Framework;
import pt.it.av.atnog.csb.entity.csb.Metric;
import pt.it.av.atnog.csb.entity.csb.PaasProvider;
import pt.it.av.atnog.csb.entity.csb.Runtime;
import pt.it.av.atnog.csb.entity.csb.ServiceVendor;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
public class KnowledgeBase {

	public static void main(String args[]) {
		ClientRequestFactory cFactory = new ClientRequestFactory();
		ClientRequest request = cFactory.createRequest("http://10.115.1.19:8080/csb/rest/paas/offerings");
		request.header("Authorization", "Basic Y2dvbmNhbHZlczpxd2VydHkxMjM=");
		request.accept(MediaType.APPLICATION_XML);
		ClientResponse<List<PaasProvider>> response;

		try {
			response = request.get(new GenericType<List<PaasProvider>>() {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting the list of PaaS providers");
		}

		if (response.getStatus() != 200) {
			throw new CSBException(Status.INTERNAL_SERVER_ERROR,
			        "Internal server error while getting the list of PaaS providers");
		}

		Set<String> paases = new HashSet<String>();
		Set<Runtime> runtimes = new HashSet<Runtime>();
		Set<Framework> frameworks = new HashSet<Framework>();
		Set<ServiceVendor> serviceVendors = new HashSet<ServiceVendor>();
		Set<Metric> metrics = new HashSet<Metric>();
		
		for (PaasProvider p : (List<PaasProvider>) response.getEntity()) {
//			System.out.format("paas(%s).\n", p.getName().toLowerCase());
			paases.add(p.getId());
			runtimes.addAll((Collection) p.getRuntimes());
			frameworks.addAll((Collection) p.getFrameworks());
			serviceVendors.addAll((Collection) p.getServiceVendors());
			metrics.addAll((Collection) p.getMetrics());
		}

		System.out.format("paases(%s).\n", paases.toString().toLowerCase());
		
//		for(Runtime r : runtimes) {
//			System.out.format("runtime(%s).\n", r.toString().toLowerCase());
//		}
//		
//		for(Framework f : frameworks) {
//			System.out.format("framework(%s).\n", f.toString().toLowerCase());
//		}
//		
//		for(PMService s : services) {
//			System.out.format("service(%s).\n", s.toString().toLowerCase());
//		}
//		
//		for(PMMetric m : metrics) {
//			System.out.format("metric(%s).\n", m.toString().toLowerCase());
//		}
		
//		System.out.format("runtimes(%s).\n", runtimes.toString().toLowerCase());
//		System.out.format("frameworks(%s).\n", frameworks.toString().toLowerCase());
//		System.out.format("services(%s).\n", services.toString().toLowerCase());
//		System.out.format("metrics(%s).\n", metrics.toString().toLowerCase());
//		for (Runtime runtime : runtimes)
//			System.out.format("runtime(%s).\n", runtime.getId().toLowerCase());
//
//		for (Framework framework : frameworks)
//			System.out.format("framework(%s).\n", framework.getId().toLowerCase());
//
//		for (PMService service : services)
//			System.out.format("service(%s).\n", service.getId().toLowerCase());
		
		for (Runtime r : runtimes)
			System.out.format("runtime(%s, '%s', '%s').\n", r.getId().toLowerCase(), r.getName().toLowerCase(), r.getVersion());

		for (Framework f : frameworks)
			System.out.format("framework(%s, '%s', '%s').\n", f.getId().toLowerCase(), f.getName().toLowerCase(), f.getVersion());

		for (ServiceVendor sv : serviceVendors)
			System.out.format("service_vendor(%s, '%s', '%s').\n", sv.getId().toLowerCase(), sv.getId().toLowerCase(), sv.getVersion());
		
		for (Metric m : metrics)
			System.out.format("metric(%s).\n", m.getName().toLowerCase());

		for (PaasProvider p : (List<PaasProvider>) response.getEntity()) {
//			System.out.print("paas_runtime("+p.getName().toLowerCase()+", [");
//			for (int i = 0; i < p.getRuntimes().size()-1; i++) {
//				System.out.print(p.getRuntimes().get(i).getId().toLowerCase()+", ");
//			}
//			System.out.print(p.getRuntimes().get(p.getRuntimes().size()-1).getId().toLowerCase());
//			System.out.println("]).");
			System.out.format("paas_runtime(%s, %s).\n", p.getId().toLowerCase(), p.getRuntimes().toString().toLowerCase());
		}
		
		for (PaasProvider p : (List<PaasProvider>) response.getEntity()) {
//			System.out.print("paas_framework("+p.getName().toLowerCase()+", [");
//			for (int i = 0; i < p.getFrameworks().size()-1; i++) {
//				System.out.print(p.getFrameworks().get(i).getId().toLowerCase()+", ");
//			}
//			System.out.print(p.getFrameworks().get(p.getFrameworks().size()-1).getId().toLowerCase());
//			System.out.println("]).");
			System.out.format("paas_framework(%s, %s).\n", p.getId().toLowerCase(), p.getFrameworks().toString().toLowerCase());
		}
		
		for (PaasProvider p : (List<PaasProvider>) response.getEntity()) {
//			System.out.print("paas_service("+p.getName().toLowerCase()+", [");
//			for (int i = 0; i < p.getServices().size()-1; i++) {
//				System.out.print(p.getServices().get(i).getId().toLowerCase()+", ");
//			}
//			System.out.print(p.getServices().get(p.getServices().size()-1).getId().toLowerCase());
//			System.out.println("]).");
			System.out.format("paas_service(%s, %s).\n", p.getId().toLowerCase(), p.getServiceVendors().toString().toLowerCase());
		}
		
		for (PaasProvider p : (List<PaasProvider>) response.getEntity()) {
//			System.out.print("paas_metric("+p.getName().toLowerCase()+", [");
//			for (int i = 0; i < p.getMonitoringMetrics().size()-1; i++) {
//				System.out.print(p.getMonitoringMetrics().get(i).getName().toLowerCase()+", ");
//			}
//			System.out.print(p.getMonitoringMetrics().get(p.getMonitoringMetrics().size()-1).getName().toLowerCase());
//			System.out.println("]).");
			System.out.format("paas_metric(%s, %s).\n", p.getId().toLowerCase(), p.getMetrics().toString().toLowerCase());
		}
	}
}
