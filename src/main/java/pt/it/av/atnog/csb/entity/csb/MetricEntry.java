package pt.it.av.atnog.csb.entity.csb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "entry")
public class MetricEntry {

	@XmlAttribute(name = "value", required = true)
	private String value;

	@XmlAttribute(name = "date", required = true)
	private String date;

	public MetricEntry() {
	}

	public MetricEntry(String value, String date) {
		this.value = value;
		this.date = date;
	}

	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the date
	 */
	public final String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public final void setDate(String date) {
		this.date = date;
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
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (!(obj instanceof MetricEntry))
			return false;
		MetricEntry other = (MetricEntry) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
