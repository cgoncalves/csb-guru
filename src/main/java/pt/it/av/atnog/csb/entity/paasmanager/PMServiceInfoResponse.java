package pt.it.av.atnog.csb.entity.paasmanager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ServiceInfoResponse")
public class PMServiceInfoResponse {
	
	@XmlElement(name = "paasProvider")
	private String paasProvider;
	
	@XmlElement(name = "appID")
	private String appId;
	
	@XmlElement(name = "serviceID")
	private String serviceId;
	
	@XmlElement(name = "serviceVendor")
	private String serviceVendor;
	
	@XmlElement(name = "serviceUsername")
	private String serviceUsername;
	
	@XmlElement(name = "servicePassword")
	private String servicePassword;
	
	@XmlElement(name = "serviceUrl")
	private String serviceUrl;

	/**
     * @return the paasProvider
     */
    public final String getPaasProvider() {
    	return paasProvider;
    }

	/**
     * @param paasProvider the paasProvider to set
     */
    public final void setPaasProvider(String paasProvider) {
    	this.paasProvider = paasProvider;
    }

	/**
     * @return the appId
     */
    public final String getAppId() {
    	return appId;
    }

	/**
     * @param appId the appId to set
     */
    public final void setAppId(String appId) {
    	this.appId = appId;
    }

	/**
     * @return the serviceId
     */
    public final String getServiceId() {
    	return serviceId;
    }

	/**
     * @param serviceId the serviceId to set
     */
    public final void setServiceId(String serviceId) {
    	this.serviceId = serviceId;
    }

	/**
     * @return the serviceVendor
     */
    public final String getServiceVendor() {
    	return serviceVendor;
    }

	/**
     * @param serviceVendor the serviceVendor to set
     */
    public final void setServiceVendor(String serviceVendor) {
    	this.serviceVendor = serviceVendor;
    }

	/**
     * @return the serviceUsername
     */
    public final String getServiceUsername() {
    	return serviceUsername;
    }

	/**
     * @param serviceUsername the serviceUsername to set
     */
    public final void setServiceUsername(String serviceUsername) {
    	this.serviceUsername = serviceUsername;
    }

	/**
     * @return the servicePassword
     */
    public final String getServicePassword() {
    	return servicePassword;
    }

	/**
     * @param servicePassword the servicePassword to set
     */
    public final void setServicePassword(String servicePassword) {
    	this.servicePassword = servicePassword;
    }

	/**
     * @return the serviceUrl
     */
    public final String getServiceUrl() {
    	return serviceUrl;
    }

	/**
     * @param serviceUrl the serviceUrl to set
     */
    public final void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}
