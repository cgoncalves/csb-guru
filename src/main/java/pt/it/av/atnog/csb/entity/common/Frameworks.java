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
@XmlRootElement(name = "frameworks")
public class Frameworks {

	@XmlElement(name = "framework")
	private List<Framework> frameworks;
	
	public Frameworks() {}
	
	public Frameworks(Framework... frameworks) {
		this.frameworks = Arrays.asList(frameworks);
	}
	

	/**
     * @return the frameworks
     */
    public final List<Framework> getFrameworks() {
    	return frameworks;
    }

	/**
     * @param frameworks the frameworks to set
     */
    public final void setFrameworks(List<Framework> frameworks) {
    	this.frameworks = frameworks;
    }

}
