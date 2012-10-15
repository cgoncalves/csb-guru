package pt.it.av.atnog.csb.entity.paasmanager;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ServiceListInfoResponse")
public class PMServiceListInfoResponse {
	
	@XmlElement(name = "ServiceInfoResponse")
	private List<PMServiceInfoResponse> services;

	/**
     * @return the services
     */
    public final List<PMServiceInfoResponse> getServices() {
    	return services;
    }

	/**
     * @param services the services to set
     */
    public final void setServices(List<PMServiceInfoResponse> services) {
    	this.services = services;
    }

}
