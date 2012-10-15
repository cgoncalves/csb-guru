package pt.it.av.atnog.csb.entity.paasmanager;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paasProvider", propOrder = { "runtimes", "frameworks", "services", "metrics" })
@XmlRootElement(name = "paasProvider")
public class PMPaasProvider {

	@XmlElementWrapper(name = "runtimes", required = true)
	@XmlElement(name = "runtime")
	protected List<PMRuntime> runtimes;

	@XmlElementWrapper(name = "frameworks", required = true)
	@XmlElement(name = "framework")
	protected List<PMFramework> frameworks;

	@XmlElementWrapper(name = "services", required = true)
	@XmlElement(name = "service")
	protected List<PMService> services;

	@XmlElementWrapper(name = "monitoringMetrics", required = true)
	@XmlElement(name = "metric")
	protected List<PMMetric> metrics;

	@XmlAttribute(required = true)
	protected String name;

	/**
	 * Gets the value of the runtimes property.
	 * 
	 * @return
	 *         possible object is {@link PMRuntimes }
	 */
	public List<PMRuntime> getRuntimes() {
		return runtimes;
	}

	/**
	 * Sets the value of the runtimes property.
	 * 
	 * @param value
	 *            allowed object is {@link PMRuntimes }
	 */
	public void setRuntimes(List<PMRuntime> value) {
		this.runtimes = value;
	}

	/**
	 * Gets the value of the frameworks property.
	 * 
	 * @return
	 *         possible object is {@link PMFrameworks }
	 */
	public List<PMFramework> getFrameworks() {
		return frameworks;
	}

	/**
	 * Sets the value of the frameworks property.
	 * 
	 * @param value
	 *            allowed object is {@link PMFrameworks }
	 */
	public void setFrameworks(List<PMFramework> value) {
		this.frameworks = value;
	}

	/**
	 * Gets the value of the services property.
	 * 
	 * @return
	 *         possible object is {@link PMServices }
	 */
	public List<PMService> getServices() {
		return services;
	}

	/**
	 * Sets the value of the services property.
	 * 
	 * @param value
	 *            allowed object is {@link PMServices }
	 */
	public void setServices(List<PMService> value) {
		this.services = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * @return the metrics
	 */
	public final List<PMMetric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics
	 *            the metrics to set
	 */
	public final void setMetrics(List<PMMetric> metrics) {
		this.metrics = metrics;
	}

}
