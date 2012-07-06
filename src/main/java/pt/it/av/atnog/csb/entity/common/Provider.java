package pt.it.av.atnog.csb.entity.common;

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
 * 
 */
@Entity
@Table(name = "providers")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "provider")
public class Provider {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id", unique = true, nullable = false)
//	@XmlElement(name="Id")
//	private int id;

	@Id
	@Column(name = "name", unique = true, nullable = false)
	@XmlElement(name="Name")
	private String name;
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provider")
//	@XmlElement(name="Apps")
//	private List<App> apps = new ArrayList<App>();
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "platform_id", nullable = true) // FIXME should be nullable. nullable now while IaaSM isn't in place!
//	@XmlElement(name="PlatformId")
//	private Platform platform;

	public Provider() {
	}

	/**
	 * @return the id
	 */
//	public final int getId() {
//		return id;
//	}

	/**
	 * @param id
	 *            the id to set
	 */
//	public final void setId(int id) {
//		this.id = id;
//	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
     * @return the apps
     */
//    public final List<App> getApps() {
//    	return apps;
//    }

	/**
     * @param apps the apps to set
     */
//    public final void setApps(List<App> apps) {
//    	this.apps = apps;
//    }

	/**
     * @return the platform
     */
//    public final Platform getPlatform() {
//    	return platform;
//    }

	/**
     * @param platform the platform to set
     */
//    public final void setPlatform(Platform platform) {
//    	this.platform = platform;
//    }
	
}
