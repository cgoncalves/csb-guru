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
@XmlRootElement(name = "runtimes")
public class PMRuntimes {

	@XmlElement(name = "runtime")
	private List<PMRuntime> runtimes;
	
	public PMRuntimes() {}
	
	public PMRuntimes(PMRuntime... runtimes) {
		this.runtimes = Arrays.asList(runtimes);
	}
	

	/**
     * @return the runtimes
     */
    public final List<PMRuntime> getRuntimes() {
    	return runtimes;
    }

	/**
     * @param runtimes the runtimes to set
     */
    public final void setRuntimes(List<PMRuntime> runtimes) {
    	this.runtimes = runtimes;
    }

}
