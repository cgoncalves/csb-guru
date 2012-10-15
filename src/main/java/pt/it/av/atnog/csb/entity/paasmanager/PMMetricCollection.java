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
@XmlRootElement(name = "metrics")
public class PMMetricCollection {
	
	@XmlAttribute(name = "date")
	private String date;
	
	@XmlElement(name = "metric")
	private List<PMMetric> metrics;

	/**
     * @return the date
     */
    public final String getDate() {
    	return date;
    }

	/**
     * @param date the date to set
     */
    public final void setDate(String date) {
    	this.date = date;
    }

	/**
     * @return the metrics
     */
    public final List<PMMetric> getMetrics() {
    	return metrics;
    }

	/**
     * @param metrics the metrics to set
     */
    public final void setMetrics(List<PMMetric> metrics) {
    	this.metrics = metrics;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((date == null) ? 0 : date.hashCode());
	    result = prime * result + ((metrics == null) ? 0 : metrics.hashCode());
	    return result;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (!(obj instanceof PMMetricCollection))
		    return false;
	    PMMetricCollection other = (PMMetricCollection) obj;
	    if (date == null) {
		    if (other.date != null)
			    return false;
	    } else if (!date.equals(other.date))
		    return false;
	    if (metrics == null) {
		    if (other.metrics != null)
			    return false;
	    } else if (!metrics.equals(other.metrics))
		    return false;
	    return true;
    }
	
}
