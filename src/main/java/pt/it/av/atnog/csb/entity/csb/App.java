package pt.it.av.atnog.csb.entity.csb;

import java.io.Serializable;
import java.util.List;

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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Entity
@Table(name = "apps")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "app")
public class App implements Serializable {

	private static final long serialVersionUID = 6435886006122046385L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement(name = "id")
	private String id;

	@Column(name = "url", unique = true, nullable = false)
	@XmlElement(name = "url")
	private String url;

	@Transient
	@XmlElement(name = "status")
	private String status;

	@Transient
	@XmlElement(name = "repository")
	private String repository;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id", nullable = true)
	@XmlElement(name = "provider")
	private Provider provider;

	@Transient
	@XmlElement(name = "memory")
	private Memory memory;

	@Transient
	@XmlElement(name = "instances")
	private int instances;
	
	@Transient
	@XmlElement(name = "framework")
	private String framework;

	@Transient
	@XmlElementWrapper(name = "services")
	@XmlElement(name = "service")
	private List<String> servicesId;

	public App() {
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the url
	 */
	public final String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
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
	 * @param status
	 *            the status to set
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
	 * @param repository
	 *            the repository to set
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

	/**
	 * @return the memory
	 */
	public Memory getMemory() {
		return memory;
	}

	/**
	 * @param memory
	 *            the memory to set
	 */
	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * @return the instances
	 */
	public int getInstances() {
		return instances;
	}

	/**
	 * @param instances
	 *            the instances to set
	 */
	public void setInstances(int instances) {
		this.instances = instances;
	}

	/**
     * @return the framework
     */
    public String getFramework() {
	    return framework;
    }

	/**
     * @param framework the framework to set
     */
    public void setFramework(String framework) {
	    this.framework = framework;
    }

	/**
	 * @return the servicesId
	 */
	public List<String> getServicesId() {
		return servicesId;
	}

	/**
	 * @param servicesId
	 *            the servicesId to set
	 */
	public void setServicesId(List<String> servicesId) {
		this.servicesId = servicesId;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((framework == null) ? 0 : framework.hashCode());
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    result = prime * result + instances;
	    result = prime * result + ((memory == null) ? 0 : memory.hashCode());
	    result = prime * result + ((provider == null) ? 0 : provider.hashCode());
	    result = prime * result + ((repository == null) ? 0 : repository.hashCode());
	    result = prime * result + ((servicesId == null) ? 0 : servicesId.hashCode());
	    result = prime * result + ((status == null) ? 0 : status.hashCode());
	    result = prime * result + ((url == null) ? 0 : url.hashCode());
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
	    if (!(obj instanceof App))
		    return false;
	    App other = (App) obj;
	    if (framework == null) {
		    if (other.framework != null)
			    return false;
	    } else if (!framework.equals(other.framework))
		    return false;
	    if (id == null) {
		    if (other.id != null)
			    return false;
	    } else if (!id.equals(other.id))
		    return false;
	    if (instances != other.instances)
		    return false;
	    if (memory == null) {
		    if (other.memory != null)
			    return false;
	    } else if (!memory.equals(other.memory))
		    return false;
	    if (provider == null) {
		    if (other.provider != null)
			    return false;
	    } else if (!provider.equals(other.provider))
		    return false;
	    if (repository == null) {
		    if (other.repository != null)
			    return false;
	    } else if (!repository.equals(other.repository))
		    return false;
	    if (servicesId == null) {
		    if (other.servicesId != null)
			    return false;
	    } else if (!servicesId.equals(other.servicesId))
		    return false;
	    if (status == null) {
		    if (other.status != null)
			    return false;
	    } else if (!status.equals(other.status))
		    return false;
	    if (url == null) {
		    if (other.url != null)
			    return false;
	    } else if (!url.equals(other.url))
		    return false;
	    return true;
    }

}
