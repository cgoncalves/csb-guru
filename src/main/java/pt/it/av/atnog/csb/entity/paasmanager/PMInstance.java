package pt.it.av.atnog.csb.entity.paasmanager;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "instance")
public class PMInstance {

	@XmlAttribute(name = "id")
	private int id;

	@XmlElement(name = "metrics")
	private List<PMMetricCollection> metricCollections;

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
	public final List<PMMetricCollection> getMetricCollections() {
		return metricCollections;
	}

	/**
	 * @param metricCollection
	 *            the metrics to set
	 */
	public final void setMetricCollections(List<PMMetricCollection> metricCollections) {
		this.metricCollections = metricCollections;
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
		result = prime * result + ((metricCollections == null) ? 0 : metricCollections.hashCode());
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
		if (!(obj instanceof PMInstance))
			return false;
		PMInstance other = (PMInstance) obj;
		if (id != other.id)
			return false;
		if (metricCollections == null) {
			if (other.metricCollections != null)
				return false;
		} else if (!metricCollections.equals(other.metricCollections))
			return false;
		return true;
	}

}
