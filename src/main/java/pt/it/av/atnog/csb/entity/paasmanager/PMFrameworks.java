package pt.it.av.atnog.csb.entity.paasmanager;

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
@XmlRootElement(name = "frameworks")
public class PMFrameworks {

	@XmlElement(name = "framework")
	private List<PMFramework> frameworks;
	
	public PMFrameworks() {}
	
	public PMFrameworks(PMFramework... frameworks) {
		this.frameworks = Arrays.asList(frameworks);
	}
	

	/**
     * @return the frameworks
     */
    public final List<PMFramework> getFrameworks() {
    	return frameworks;
    }

	/**
     * @param frameworks the frameworks to set
     */
    public final void setFrameworks(List<PMFramework> frameworks) {
    	this.frameworks = frameworks;
    }

}
