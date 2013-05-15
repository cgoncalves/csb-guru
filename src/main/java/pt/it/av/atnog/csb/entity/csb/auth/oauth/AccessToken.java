package pt.it.av.atnog.csb.entity.csb.auth.oauth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pt.it.av.atnog.csb.entity.csb.User;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Entity
@Table(name = "access_tokens")
public class AccessToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@ManyToOne
	@JoinColumn(name = "consumer_key", nullable = true, referencedColumnName = "`key`")
	private Consumer consumer;

	@ManyToOne
	@JoinColumn(name="user_id", nullable = false, unique = false)
	private User user;

	@Column(name = "token", unique = true, nullable = false)
	private String token;

	@Column(name = "secret", unique = true, nullable = false)
	private String secret;

//	@Column(name = "scopes", unique = false, nullable = true)
//	private String scopes;

//	@ManyToMany
//	@Column(name = "permissions", unique = false, nullable = true)
//	private Set<Permission> permissions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumerKey(Consumer consumer) {
		this.consumer = consumer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

//	public String getScopes() {
//		return scopes;
//	}
//
//	public void setScopes(String scopes) {
//		this.scopes = scopes;
//	}
//
//	public Set<Permission> getPermissions() {
//		return permissions;
//	}
//
//	public void setPermissions(Set<Permission> permissions) {
//		this.permissions = permissions;
//	}
}
