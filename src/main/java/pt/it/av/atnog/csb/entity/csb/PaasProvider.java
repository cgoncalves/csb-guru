package pt.it.av.atnog.csb.entity.csb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "paas_provider", propOrder = { "id", "runtimes", "frameworks", "serviceVendors", "metrics" })
@XmlRootElement(name = "paas_provider")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class PaasProvider {

	@XmlElement(required = true)
	@JsonProperty("id")
	private String id;
	
	@XmlElementWrapper(name = "runtimes", required = true)
	@XmlElement(name = "runtime")
	@JsonProperty("runtimes")
	private List<Runtime> runtimes;

	@XmlElementWrapper(name = "frameworks", required = true)
	@XmlElement(name = "framework")
	@JsonProperty("frameworks")
	private List<Framework> frameworks;

	@XmlElementWrapper(name = "service_vendors", required = true)
	@XmlElement(name = "service_vendor")
	@JsonProperty("service_vendors")
	private List<ServiceVendor> serviceVendors;

	@XmlElementWrapper(name = "metrics", required = true)
	@XmlElement(name = "metric")
	@JsonProperty("metrics")
	private List<Metric> metrics;

	/**
	 * Gets the value of the runtimes property.
	 * 
	 * @return
	 *         possible object is {@link Runtimes }
	 */
	public List<Runtime> getRuntimes() {
		return runtimes;
	}

	/**
	 * Sets the value of the runtimes property.
	 * 
	 * @param value
	 *            allowed object is {@link Runtimes }
	 */
	public void setRuntimes(List<Runtime> value) {
		this.runtimes = value;
	}

	/**
	 * Gets the value of the frameworks property.
	 * 
	 * @return
	 *         possible object is {@link Frameworks }
	 */
	public List<Framework> getFrameworks() {
		return frameworks;
	}

	/**
	 * Sets the value of the frameworks property.
	 * 
	 * @param value
	 *            allowed object is {@link Frameworks }
	 */
	public void setFrameworks(List<Framework> value) {
		this.frameworks = value;
	}

	/**
	 * Gets the value of the service vendor property.
	 * 
	 * @return
	 *         possible object is {@link Services }
	 */
	public List<ServiceVendor> getServiceVendors() {
		return serviceVendors;
	}

	/**
	 * Sets the value of the service vendors property.
	 * 
	 * @param value
	 *            allowed object is {@link Services }
	 */
	public void setServiceVendors(List<ServiceVendor> serviceVendors) {
		this.serviceVendors = serviceVendors;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * @return the metrics
	 */
	public final List<Metric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics
	 *            the metrics to set
	 */
	public final void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

}
