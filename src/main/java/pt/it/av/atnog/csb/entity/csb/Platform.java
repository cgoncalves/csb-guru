package pt.it.av.atnog.csb.entity.csb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
@Entity
@Table(name = "platforms")
@XmlRootElement(name = "Platform")
public class Platform {

//	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@XmlAttribute(name="Id")
	private int id;
	
	@Id
	@Column(name = "name", unique = true, nullable = false)
	@XmlAttribute(name="Name")
	private String name;
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "platform")
//	@XmlElement(name="ProvidersId")
//	private List<Provider> providers = new ArrayList<Provider>();
	
//	/**
//	 * @return the id
//	 */
//	public final int getId() {
//		return id;
//	}
//
//	/**
//	 * @param id
//	 *            the id to set
//	 */
//	public final void setId(int id) {
//		this.id = id;
//	}
//
//	/**
//	 * @return the name
//	 */
//	public final String getName() {
//		return name;
//	}
//
//	/**
//	 * @param name
//	 *            the name to set
//	 */
//	public final void setName(String name) {
//		this.name = name;
//	}
//
//	/**
//     * @return the providers
//     */
//    public final Set<Provider> getProviders() {
//    	return providers;
//    }
//
//	/**
//     * @param providers the providers to set
//     */
//    public final void setProviders(Set<Provider> providers) {
//    	this.providers = providers;
//    }

}
