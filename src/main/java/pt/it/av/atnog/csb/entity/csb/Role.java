package pt.it.av.atnog.csb.entity.csb;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Carlos Gonçalves
 *
 */

@Entity
@Table(name = "roles")
@NamedQueries({
	@NamedQuery(name = "Role.findByName", query = "SELECT OBJECT(role) FROM Role role WHERE role.name = :name")
})
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "role")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class Role {
	
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
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<User> users;

	public Role() {
		
	}
	
	public Role(String name) {
		super();
		this.name = name;
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
