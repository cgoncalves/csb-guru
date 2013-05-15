package pt.it.av.atnog.csb.entity.csb.auth.oauth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Entity
@Table(name = "consumers")
public class Consumer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;
	
	@Column(name = "`key`", unique = true, nullable = false)
	private String key;
	
	@Column(name = "secret", unique = true, nullable = false)
	private String secret;
	
	@Column(name = "display_name", unique = false, nullable = false)
	private String displayName;
	
	@Column(name = "connect_uri", unique = false, nullable = true)
	private String connectUri;
	
//	@Column(name = "scopes", unique = false, nullable = true)
//	private String scopes;
//	
//	@ManyToMany
//	@Column(name = "permissions", unique = false, nullable = true)
//	private Set<Permission> permissions;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getConnectUri() {
		return connectUri;
	}

	public void setConnectUri(String connectUri) {
		this.connectUri = connectUri;
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
