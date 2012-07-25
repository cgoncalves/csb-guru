package pt.it.av.atnog.csb.entity.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "paasProvider",
    "appID",
    "appLog",
    "appUrl"
})
@XmlRootElement(name = "ApplicationLogsResponse")
public class ApplicationLogsResponse {

    @XmlElement(required = true)
    protected String paasProvider;
    @XmlElement(required = true)
    protected String appID;
    @XmlElement(required = true)
    protected String appLog;
    @XmlElement(required = true)
    protected String appUrl;

    public ApplicationLogsResponse() {
    	
    }
    
    public ApplicationLogsResponse(String appId) {
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
     * Gets the value of the appLog property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppLog() {
        return appLog;
    }

    /**
     * Sets the value of the appLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppLog(String value) {
        this.appLog = value;
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
