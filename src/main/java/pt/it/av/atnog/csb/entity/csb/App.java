package pt.it.av.atnog.csb.entity.csb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pt.it.av.atnog.csb.entity.common.Provider;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Entity
@Table(name = "apps")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "App")
public class App {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id", unique = true, nullable = false)
//	@XmlElement(name="Id")
//	private int id;

	@Id
	@Column(name = "name", unique = true, nullable = false)
	@XmlElement(name="Name")
	private String name;
	
	@Column(name = "url", unique = true, nullable = false)
	@XmlElement(name="Url")
	private String url;
	
	@Transient
	@XmlElement(name="Status")
	private String status;
	
	@Transient
	@XmlElement(name="Repository")
	private String repository;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id", nullable = true)
	@XmlElement(name="Provider")
	private Provider provider;

	public App() {
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
     * @return the url
     */
    public final String getUrl() {
    	return url;
    }

	/**
     * @param url the url to set
     */
    public final void setUrl(String url) {
    	this.url = url;
    }

	/**
     * @return the status
     */
    public final String getStatus() {
    	return status;
    }

	/**
     * @param status the status to set
     */
    public final void setStatus(String status) {
    	this.status = status;
    }

	/**
     * @return the repository
     */
    public final String getRepository() {
    	return repository;
    }

	/**
     * @param repository the repository to set
     */
    public final void setRepository(String repository) {
    	this.repository = repository;
    }

	/**
	 * @return the provider
	 */
	public final Provider getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public final void setProvider(Provider provider) {
		this.provider = provider;
	}

}
