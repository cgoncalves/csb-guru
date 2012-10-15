package pt.it.av.atnog.csb.entity.paasmanager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "metric", propOrder = { "name", "info", "value", "unit" })
@XmlRootElement(name = "metric")
public class PMMetric {

	@XmlElement(name = "name", required = true)
	private String name;
	
	@XmlElement(name = "info", required = false)
	private String info;
	
	@XmlElement(name = "value", required = true)
	private String value;
	
	@XmlElement(name = "unit", required = true)
	private String unit;
	
	public PMMetric() {
	}

	/**
	 * @param name
	 * @param info
	 */
	public PMMetric(String name, String info, String value, String unit) {
		super();
		this.name = name;
		this.info = info;
		this.value = value;
		this.unit = unit;
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
     * @return the metricValue
     */
    public final String getValue() {
    	return value;
    }

	/**
     * @param metricValue the metricValue to set
     */
    public final void setValue(String value) {
    	this.value = value;
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
