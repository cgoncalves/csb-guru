package pt.it.av.atnog.csb.entity.paasmanager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "paasProvider",
    "appID",
    "appStatus",
    "appUrl"
})
@XmlRootElement(name = "ApplicationDeployResponse")
public class PMApplicationDeployResponse {

    @XmlElement(required = true)
    protected String paasProvider;
    @XmlElement(required = true)
    protected String appID;
    @XmlElement(required = true)
    protected String appStatus;
    @XmlElement(required = true)
    protected String appUrl;
    
    public PMApplicationDeployResponse() {
    	
    }
    
    public PMApplicationDeployResponse(String appId) {
    	this.appID = appId;
    }

    /**
     * Gets the value of the paasProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaasProvider() {
        return paasProvider;
    }

    /**
     * Sets the value of the paasProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaasProvider(String value) {
        this.paasProvider = value;
    }

    /**
     * Gets the value of the appID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppID() {
        return appID;
    }

    /**
     * Sets the value of the appID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppID(String value) {
        this.appID = value;
    }

    /**
     * Gets the value of the appStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppStatus() {
        return appStatus;
    }

    /**
     * Sets the value of the appStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppStatus(String value) {
        this.appStatus = value;
    }

    /**
     * Gets the value of the appUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppUrl() {
        return appUrl;
    }

    /**
     * Sets the value of the appUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppUrl(String value) {
        this.appUrl = value;
    }

}
