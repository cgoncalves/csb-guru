package pt.it.av.atnog.csb.entity.csb;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(name = "User.findById", query = "SELECT OBJECT(user) FROM User user WHERE user.id = :id"),
	@NamedQuery(name = "User.findByName", query = "SELECT OBJECT(user) FROM User user WHERE user.name = :name"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT OBJECT(user) FROM User user WHERE user.email = :email")
})

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "user")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@XmlElement(name = "id")
	@JsonProperty("id")
	private int id;
	
	@Column(name = "name", nullable = false, unique = true)
	@XmlElement(name = "name")
	@JsonProperty("name")
	private String name;

	@Column(name = "password", unique = false, nullable = false)
//	@XmlElement(name = "password")
//	@JsonProperty("password")
	@XmlTransient
	@JsonIgnore
	private String password;

	@Column(name = "email", unique = true, nullable = false)
	@XmlElement(name = "email")
	@JsonProperty("email")
	private String email;

	
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "users_roles",
		joinColumns = { @JoinColumn(name = "user_id") },
		inverseJoinColumns = { @JoinColumn(name = "role_id") }
	)
	@XmlElement(name = "roles")
	@JsonProperty("roles")
	private Set<Role> roles;

	public User() {

	}
	
	public User(String name, String password, String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}
	
	public User(String name, String password, String email, Set<Role> roles) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
