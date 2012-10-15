package pt.it.av.atnog.csb.entity.csb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Entity
@Table(name = "providers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "provider")
public class Provider {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement(name = "id")
	private String id;

	// @Column(name = "name", unique = true, nullable = false)
	// @XmlElement(name = "Name")
	// private String name;

	// @Column(name = "type", unique = false, nullable = false)
	// @XmlElement(name = "type")
	// private String type;

	public Provider() {
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	// /**
	// * @return the name
	// */
	// public final String getName() {
	// return name;
	// }
	//
	// /**
	// * @param name
	// * the name to set
	// */
	// public final void setName(String name) {
	// this.name = name;
	// }

	// /**
	// * @return the type
	// */
	// public final String getType() {
	// return type;
	// }
	//
	// /**
	// * @param type
	// * the type to set
	// */
	// public final void setType(String type) {
	// this.type = type;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Provider))
			return false;
		Provider other = (Provider) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
