package pt.it.av.atnog.csb.entity.common;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paasProvider", propOrder = {
    "runtimes",
    "frameworks",
    "services"
})
@XmlRootElement(name = "paasProvider")
public class PaasProvider {

	@XmlElementWrapper(name = "runtimes", required = true)
	@XmlElement(name="runtime")
    protected List<Runtime> runtimes;
	
	@XmlElementWrapper(name = "frameworks", required = true)
	@XmlElement(name="framework")
    protected List<Framework> frameworks;
	
	@XmlElementWrapper(name = "services", required = true)
	@XmlElement(name="service")
    protected List<Service> services;
	
    @XmlAttribute(required = true)
    protected String name;
    
    @XmlAttribute
    protected Integer score;

    /**
     * Gets the value of the runtimes property.
     * 
     * @return
     *     possible object is
     *     {@link Runtimes }
     *     
     */
    public List<Runtime> getRuntimes() {
        return runtimes;
    }

    /**
     * Sets the value of the runtimes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Runtimes }
     *     
     */
    public void setRuntimes(List<Runtime> value) {
        this.runtimes = value;
    }

    /**
     * Gets the value of the frameworks property.
     * 
     * @return
     *     possible object is
     *     {@link Frameworks }
     *     
     */
    public List<Framework> getFrameworks() {
        return frameworks;
    }

    /**
     * Sets the value of the frameworks property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frameworks }
     *     
     */
    public void setFrameworks(List<Framework> value) {
        this.frameworks = value;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setScore(Integer value) {
        this.score = value;
    }

}
