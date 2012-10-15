package pt.it.av.atnog.csb.entity.paasmanager;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ApplicationStatisticsResponse")
public class PMApplicationStatisticsResponse {

	@XmlElement(name = "appID")
	private String appId;

	@XmlElementWrapper(name = "instances")
	@XmlElement(name = "instance")
	private List<PMInstance> instances;

	/**
	 * @return the appId
	 */
	public final String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public final void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the instances
	 */
	public final List<PMInstance> getInstances() {
		return instances;
	}

	/**
	 * @param instances
	 *            the instances to set
	 */
	public final void setInstances(List<PMInstance> instances) {
		this.instances = instances;
	}
}
