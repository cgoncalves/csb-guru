package pt.it.av.atnog.csb.entity.csb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Entity
@Table(name = "paas_providers")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "paas_provider", propOrder = { "id", "runtimes", "frameworks", "serviceVendors", "metrics" })
@XmlRootElement(name = "paas_provider")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class PaasProvider {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement(required = true)
	@JsonProperty("id")
	private String id;
	
	@XmlElementWrapper(name = "runtimes", required = true)
	@XmlElement(name = "runtime")
	@JsonProperty("runtimes")
	@Transient
	private List<Runtime> runtimes;

	@XmlElementWrapper(name = "frameworks", required = true)
	@XmlElement(name = "framework")
	@JsonProperty("frameworks")
	@Transient
	private List<Framework> frameworks;

	@XmlElementWrapper(name = "service_vendors", required = true)
	@XmlElement(name = "service_vendor")
	@JsonProperty("service_vendors")
	@Transient
	private List<ServiceVendor> serviceVendors;

	@XmlElementWrapper(name = "metrics", required = true)
	@XmlElement(name = "metric")
	@JsonProperty("metrics")
	@Transient
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frameworks == null) ? 0 : frameworks.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((metrics == null) ? 0 : metrics.hashCode());
		result = prime * result + ((runtimes == null) ? 0 : runtimes.hashCode());
		result = prime * result + ((serviceVendors == null) ? 0 : serviceVendors.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaasProvider other = (PaasProvider) obj;
		if (frameworks == null) {
			if (other.frameworks != null)
				return false;
		} else if (!frameworks.equals(other.frameworks))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (metrics == null) {
			if (other.metrics != null)
				return false;
		} else if (!metrics.equals(other.metrics))
			return false;
		if (runtimes == null) {
			if (other.runtimes != null)
				return false;
		} else if (!runtimes.equals(other.runtimes))
			return false;
		if (serviceVendors == null) {
			if (other.serviceVendors != null)
				return false;
		} else if (!serviceVendors.equals(other.serviceVendors))
			return false;
		return true;
	}

}
