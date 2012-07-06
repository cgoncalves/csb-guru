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
@XmlRootElement(name = "runtimes")
public class Runtimes {

	@XmlElement(name = "runtime")
	private List<Runtime> runtimes;
	
	public Runtimes() {}
	
	public Runtimes(Runtime... runtimes) {
		this.runtimes = Arrays.asList(runtimes);
	}
	

	/**
     * @return the runtimes
     */
    public final List<Runtime> getRuntimes() {
    	return runtimes;
    }

	/**
     * @param runtimes the runtimes to set
     */
    public final void setRuntimes(List<Runtime> runtimes) {
    	this.runtimes = runtimes;
    }

}
