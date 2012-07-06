
package pt.it.av.atnog.csb.entity.csb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pt.it.av.atnog.csb.entity.common.Framework;
import pt.it.av.atnog.csb.entity.common.Runtime;
import pt.it.av.atnog.csb.entity.common.Service;
import pt.it.av.atnog.csb.entity.common.Services;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="runtimes" type="{}runtimes"/>
 *         &lt;element name="frameworks" type="{}frameworks"/>
 *         &lt;element name="services" type="{}services"/>
 *       &lt;/sequence>
 *       &lt;attribute name="provider" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "runtime",
    "framework",
    "services"
})
@XmlRootElement(name = "Manifest")
public class Manifest {

	@XmlElement(name="runtime", required = true)
    protected Runtime runtime;
	
	@XmlElement(name="framework", required = true)
    protected Framework framework;
	
	@XmlElementWrapper(name = "services", required = true)
	@XmlElement(name="service")
    protected List<Service> services;
	
    @XmlAttribute
    protected String provider;

    /**
     * @return the runtime
     */
    public final Runtime getRuntime() {
    	return runtime;
    }

	/**
     * @param runtime the runtime to set
     */
    public final void setRuntime(Runtime runtime) {
    	this.runtime = runtime;
    }

	/**
     * @return the framework
     */
    public final Framework getFramework() {
    	return framework;
    }

	/**
     * @param framework the framework to set
     */
    public final void setFramework(Framework framework) {
    	this.framework = framework;
    }

	/**
     * Gets the value of the services property.
     * 
     * @return
     *     possible object is
     *     {@link Services }
     *     
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * Sets the value of the services property.
     * 
     * @param value
     *     allowed object is
     *     {@link Services }
     *     
     */
    public void setServices(List<Service> value) {
        this.services = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
		this.provider = value;
	}

}
