package pt.it.av.atnog.csb.entity.paasmanager;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pt.it.av.atnog.csb.entity.csb.Memory;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "paasProvider", "appID", "appStatus", "appUrl", "appMemory", "appInstances",
        "appFramework", "appServicesId" })
@XmlRootElement(name = "ApplicationInfoResponse")
public class PMApplicationInfoResponse {

	@XmlElement(required = true)
	private String paasProvider;
	
	@XmlElement(required = true)
	private String appID;
	
	@XmlElement(required = true)
	private String appStatus;
	
	@XmlElement(required = true)
	private String appUrl;
	
	@XmlElement(required = true)
	private PMAppMemory appMemory;
	
	@XmlElement(required = true)
	private int appInstances;
	
	@XmlElement(required = true)
	private String appFramework;
	
	@XmlElementWrapper(name = "appServices", required = true)
	@XmlElement(name = "serviceID")
	private List<String> appServicesId;

	public PMApplicationInfoResponse() {

	}

	public PMApplicationInfoResponse(String appId) {
		this.appID = appId;
	}

	/**
	 * Gets the value of the paasProvider property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getPaasProvider() {
		return paasProvider;
	}

	/**
	 * Sets the value of the paasProvider property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setPaasProvider(String value) {
		this.paasProvider = value;
	}

	/**
	 * Gets the value of the appID property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getAppID() {
		return appID;
	}

	/**
	 * Sets the value of the appID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setAppID(String value) {
		this.appID = value;
	}

	/**
	 * Gets the value of the appStatus property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getAppStatus() {
		return appStatus;
	}

	/**
	 * Sets the value of the appStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setAppStatus(String value) {
		this.appStatus = value;
	}

	/**
	 * Gets the value of the appUrl property.
	 * 
	 * @return possible object is {@link String }
	 */
	public String getAppUrl() {
		return appUrl;
	}

	/**
	 * Sets the value of the appUrl property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setAppUrl(String value) {
		this.appUrl = value;
	}

	/**
	 * @return the appMemory
	 */
	public final PMAppMemory getAppMemory() {
		return appMemory;
	}

	/**
	 * @param appMemory
	 *            the appMemory to set
	 */
	public final void setAppMemory(PMAppMemory appMemory) {
		this.appMemory = appMemory;
	}

	/**
	 * @return the appInstances
	 */
	public final int getAppInstances() {
		return appInstances;
	}

	/**
	 * @param appInstances
	 *            the appInstances to set
	 */
	public final void setAppInstances(int appInstances) {
		this.appInstances = appInstances;
	}

	/**
	 * @return the appFramework
	 */
	public final String getAppFramework() {
		return appFramework;
	}

	/**
	 * @param appFramework
	 *            the appFramework to set
	 */
	public final void setAppFramework(String appStack) {
		this.appFramework = appStack;
	}

	/**
	 * @return the appServicesId
	 */
	public final List<String> getAppServicesId() {
		return appServicesId;
	}

	/**
	 * @param appServicesId
	 *            the appServicesId to set
	 */
	public final void setAppServicesId(List<String> appServicesId) {
		this.appServicesId = appServicesId;
	}
}
