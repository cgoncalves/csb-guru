package pt.it.av.atnog.csb.entity.common;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "services")
public class Services {

	@XmlElement(name = "service")
	private List<Service> services;
	
	public Services() {}
	
	public Services(Service... services) {
		this.services = Arrays.asList(services);
	}
	

	/**
     * @return the services
     */
    public final List<Service> getServices() {
    	return services;
    }

	/**
     * @param services the services to set
     */
    public final void setServices(List<Service> services) {
    	this.services = services;
    }

}
