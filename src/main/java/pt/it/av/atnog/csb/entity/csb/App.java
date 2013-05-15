package pt.it.av.atnog.csb.entity.csb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@Entity
@Table(name = "apps")
@NamedQueries({
	@NamedQuery(name = "App.findById", query = "SELECT apps FROM App apps WHERE apps.id = :id"),
	@NamedQuery(name = "App.findAllByUserId", query = "SELECT apps FROM App apps WHERE apps.user.id = :user_id"),
	@NamedQuery(name = "App.findAllByUserName", query = "SELECT apps FROM App apps WHERE apps.user.name = :user_name"),
	@NamedQuery(name = "App.findAllDeployPending", query = "SELECT apps FROM App apps WHERE apps.deployState = 0")
})
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "app")
@JsonAutoDetect(getterVisibility=Visibility.NONE)
public class App implements Serializable {
	
	public static enum DeployState {
		PENDING, DEPLOYING, DEPLOYED
	}

	private static final long serialVersionUID = 6435886006122046385L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement(name = "id")
	@JsonProperty("id")
	private String id;

	@Column(name = "url", unique = true, nullable = false)
	@XmlElement(name = "url")
	@JsonProperty("url")
	private String url;

	@Transient
	@XmlElement(name = "status")
	@JsonProperty("status")
	private String status;

	@Transient
	@XmlElement(name = "repository")
	@JsonProperty("repository")
	private String repository;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id", nullable = true)
	@XmlElement(name = "provider")
	@JsonProperty("provider")
	private PaasProvider provider;

	@Transient
	@XmlElement(name = "memory")
	@JsonProperty("memory")
	private Memory memory;

	@Transient
	@XmlElement(name = "instances")
	@JsonProperty("instances")
	private int instances;
	
	@Transient
	@XmlElement(name = "framework_id")
	@JsonProperty("framework_id")
	private String frameworkId;

	@Transient
	@XmlElementWrapper(name = "services_id")
	@XmlElement(name = "service_id")
	@JsonProperty("services_id")
	private List<String> servicesId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@XmlTransient
	@JsonIgnore
	private User user;
	
	@Column(name = "repository_type", unique = false, nullable = false)
	@XmlElement(name = "repository_type")
	@JsonProperty("repository_type")
	private String repositoryType;
	
	@Column(name = "deployed", unique = false, nullable = true)
	@XmlTransient
	@JsonIgnore
	private DeployState deployState = DeployState.PENDING;

	public App() {
	}
	
	public App(String id, User user) {
		this.id = id;
		this.user = user;
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
	public final PaasProvider getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public final void setProvider(PaasProvider provider) {
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
     * @return the frameworkId
     */
    public String getFramework() {
	    return frameworkId;
    }

	/**
     * @param frameworkId the frameworkId to set
     */
    public void setFrameworkId(String frameworkId) {
	    this.frameworkId = frameworkId;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(String repositoryType) {
		this.repositoryType = repositoryType;
	}

	public DeployState getDeployState() {
		return deployState;
	}

	public void setDeployState(DeployState deployState) {
		this.deployState = deployState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deployState == null) ? 0 : deployState.hashCode());
		result = prime * result
				+ ((frameworkId == null) ? 0 : frameworkId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + instances;
		result = prime * result + ((memory == null) ? 0 : memory.hashCode());
		result = prime * result
				+ ((provider == null) ? 0 : provider.hashCode());
		result = prime * result
				+ ((repository == null) ? 0 : repository.hashCode());
		result = prime * result
				+ ((repositoryType == null) ? 0 : repositoryType.hashCode());
		result = prime * result
				+ ((servicesId == null) ? 0 : servicesId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		App other = (App) obj;
		if (deployState != other.deployState)
			return false;
		if (frameworkId == null) {
			if (other.frameworkId != null)
				return false;
		} else if (!frameworkId.equals(other.frameworkId))
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
		if (repositoryType == null) {
			if (other.repositoryType != null)
				return false;
		} else if (!repositoryType.equals(other.repositoryType))
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
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
