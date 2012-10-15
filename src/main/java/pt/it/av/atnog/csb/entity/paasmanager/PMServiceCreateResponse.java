package pt.it.av.atnog.csb.entity.paasmanager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "paasProvider", "appID", "serviceID", "serviceStatus" })
@XmlRootElement(name = "ServiceCreateResponse")
public class PMServiceCreateResponse {

	@XmlElement(required = true)
	protected String paasProvider;
	@XmlElement(required = true)
	protected String appID;
	@XmlElement(required = true)
	protected String serviceID;
	@XmlElement(required = true)
	protected String serviceStatus;

	/**
	 * @return the paasProvider
	 */
	public final String getPaasProvider() {
		return paasProvider;
	}

	/**
	 * @param paasProvider
	 *            the paasProvider to set
	 */
	public final void setPaasProvider(String paasProvider) {
		this.paasProvider = paasProvider;
	}

	/**
	 * @return the appID
	 */
	public final String getAppID() {
		return appID;
	}

	/**
	 * @param appID
	 *            the appID to set
	 */
	public final void setAppID(String appID) {
		this.appID = appID;
	}

	/**
	 * @return the serviceID
	 */
	public final String getServiceID() {
		return serviceID;
	}

	/**
	 * @param serviceID
	 *            the serviceID to set
	 */
	public final void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	/**
	 * @return the serviceStatus
	 */
	public final String getServiceStatus() {
		return serviceStatus;
	}

	/**
	 * @param serviceStatus
	 *            the serviceStatus to set
	 */
	public final void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

}
