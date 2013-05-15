package pt.it.av.atnog.csb.entity.paasmanager;

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


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "providerId", "id", "status", "url", "memory", "instances",
        "frameworkId", "servicesId" })
@XmlRootElement(name = "ApplicationInfoResponse")
@JsonAutoDetect(getterVisibility=Visibility.NONE)
public class PMApplicationInfoResponse {

	@XmlElement(required = true)
	@JsonProperty("provider_id")
	private String providerId;
	
	@XmlElement(required = true)
	@JsonProperty("id")
	private String id;
	
	@XmlElement(required = true)
	@JsonProperty("status")
	private String status;
	
	@XmlElement(required = true)
	@JsonProperty("url")
	private String url;
	
	@XmlElement(required = true)
	@JsonProperty("memory")
	private PMAppMemory memory;
	
	@XmlElement(required = true)
	@JsonProperty("instances")
	private int instances;
	
	@XmlElement(required = true)
	@JsonProperty("framework_id")
	private String frameworkId;
	
	@XmlElementWrapper(name = "services", required = true)
	@XmlElement(name = "service_id")
	@JsonProperty("services_id")
	private List<String> servicesId;

	public PMApplicationInfoResponse() {

	}

	public PMApplicationInfoResponse(String id) {
		this.id = id;
	}

	/**
	 * Gets the value of the providerId property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getProvider() {
		return providerId;
	}

	/**
	 * Sets the value of the providerId property.
	 * 
	 * @param providerId
	 *            allowed object is {@link String }
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param id
	 *            allowed object is {@link String }
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the value of the status property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the value of the status property.
	 * 
	 * @param status
	 *            allowed object is {@link String }
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the value of the url property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the value of the url property.
	 * 
	 * @param url
	 *            allowed object is {@link String }
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the memory
	 */
	public final PMAppMemory getMemory() {
		return memory;
	}

	/**
	 * @param memory
	 *            the memory to set
	 */
	public final void setMemory(PMAppMemory memory) {
		this.memory = memory;
	}

	/**
	 * @return the instances
	 */
	public final int getInstances() {
		return instances;
	}

	/**
	 * @param instances
	 *            the instances to set
	 */
	public final void setInstances(int instances) {
		this.instances = instances;
	}

	/**
	 * @return the frameworkId
	 */
	public final String getFrameworkId() {
		return frameworkId;
	}

	/**
	 * @param frameworkId
	 *            the frameworkId to set
	 */
	public final void setFrameworkId(String frameworkId) {
		this.frameworkId = frameworkId;
	}

	/**
	 * @return the servicesId
	 */
	public final List<String> getServicesId() {
		return servicesId;
	}

	/**
	 * @param servicesId
	 *            the servicesId to set
	 */
	public final void setServicesId(List<String> servicesId) {
		this.servicesId = servicesId;
	}
}
