package pt.it.av.atnog.csb.entity.csb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "metric", propOrder = { "name", "info", "unit", "metricEntries" })
@XmlRootElement(name = "metric")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class Metric {

	@XmlAttribute(name = "name", required = true)
	@JsonProperty("name")
	private String name;
	
	@XmlAttribute(name = "info", required = false)
	@JsonProperty("info")
	private String info;
	
	@XmlAttribute(name = "unit", required = true)
	@JsonProperty("unit")
	private String unit;
	
	@XmlElementWrapper(name = "entries")
	@XmlElement(name = "entry")
	@JsonProperty("entries")
	private List<MetricEntry> metricEntries;
	
	public Metric() {
	}

	public Metric(String name, String info, String unit) {
		super();
		this.name = name;
		this.info = info;
		this.unit = unit;
	}
	
	public Metric(String name, String info, String unit, List<MetricEntry> metricEntries) {
		super();
		this.metricEntries = metricEntries;
	}

	/**
     * @return the name
     */
    public final String getName() {
    	return name;
    }

	/**
     * @param name the name to set
     */
    public final void setName(String name) {
    	this.name = name;
    }

	/**
     * @return the info
     */
    public final String getInfo() {
    	return info;
    }

	/**
     * @param info the info to set
     */
    public final void setInfo(String info) {
    	this.info = info;
    }

	/**
     * @return the unit
     */
    public final String getUnit() {
    	return unit;
    }

	/**
     * @param unit the unit to set
     */
    public final void setUnit(String unit) {
    	this.unit = unit;
    }

	/**
     * @return the metricEntries
     */
    public final List<MetricEntry> getMetricEntries() {
    	return metricEntries;
    }

	/**
     * @param metricEntries the metricEntries to set
     */
    public final void setMetricEntries(List<MetricEntry> metricEntries) {
    	this.metricEntries = metricEntries;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
