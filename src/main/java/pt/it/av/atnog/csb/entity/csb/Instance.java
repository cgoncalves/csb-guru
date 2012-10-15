package pt.it.av.atnog.csb.entity.csb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "instance")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class Instance {

	@XmlAttribute(name = "id")
	@JsonProperty("id")
	private int id;

	@XmlElementWrapper(name = "metrics")
	@XmlElement(name = "metric")
	@JsonProperty("metrics")
	private List<Metric> metrics;

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the metrics
	 */
	public final List<Metric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics
	 *            the metrics to set
	 */
	public final void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((metrics == null) ? 0 : metrics.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Instance))
			return false;
		Instance other = (Instance) obj;
		if (id != other.id)
			return false;
		if (metrics == null) {
			if (other.metrics != null)
				return false;
		} else if (!metrics.equals(other.metrics))
			return false;
		return true;
	}

}
