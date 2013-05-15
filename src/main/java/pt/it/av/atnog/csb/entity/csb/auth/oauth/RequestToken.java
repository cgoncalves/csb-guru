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
@Table(name = "request_tokens")
public class RequestToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "consumer_key", nullable = true, referencedColumnName = "`key`")
	private Consumer consumer;
	
	@Column(name = "token", unique = false, nullable = false)
	private String token;
	
	@Column(name = "secret", unique = true, nullable = false)
	private String secret;
	
	@Column(name = "callback", unique = false, nullable = true)
	private String callback;
	
//	@Column(name = "scopes", unique = false, nullable = true)
//	private String scopes;
//	
//	@ManyToMany
//	@Column(name = "permissions", unique = true, nullable = true)
//	private Set<Permission> permissions;
	
	@Column(name = "verifier", unique = true, nullable = true)
	private String verifier;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

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

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
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

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
