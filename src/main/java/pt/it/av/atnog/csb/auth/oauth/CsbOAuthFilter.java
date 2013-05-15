package pt.it.av.atnog.csb.auth.oauth;

import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.jboss.resteasy.auth.oauth.OAuthConsumer;
import org.jboss.resteasy.auth.oauth.OAuthFilter;
import org.jboss.resteasy.auth.oauth.OAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public class CsbOAuthFilter extends OAuthFilter {	
	private static final Connection conn;
	private static final String DB_DRIVER;
	private static final String DB_URL;
	private static final String DB_USERNAME;
	private static final String DB_PASSWORD;
	private static final Logger logger = LoggerFactory.getLogger(CsbOAuthFilter.class);
	
	static {
		try {
			Properties props = new Properties();
			props.load(CsbOAuthProviderImpl.class.getResourceAsStream("/db.properties"));

			DB_DRIVER = props.getProperty("db.driver");
			DB_URL = props.getProperty("db.url");
			DB_USERNAME = props.getProperty("db.username");
			DB_PASSWORD = props.getProperty("db.password");

			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("db.properties resource is not available");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("In memory OAuth DB can not be created " + ex.getMessage());
		}
	}

	protected HttpServletRequest createSecurityContext(HttpServletRequest request, OAuthConsumer consumer, OAuthToken accessToken) {
		if (accessToken != null) {
			try {				
				final String userName = getUserName(accessToken);
				final Set<String> roles = getRoles(accessToken);
				logger.debug("User {} is enrolled in roles {}", userName, roles);
				
				request = new HttpServletRequestWrapper(request) {
					@Override
					public Principal getUserPrincipal() {
						return new SimplePrincipal(userName);
					}
	
					@Override
					public boolean isUserInRole(String role) {
						return roles.contains(role);
					}
	
					@Override
					public String getAuthType() {
						return OAUTH_AUTH_METHOD;
					}
				};
			} catch (SQLException e) {
				logger.error("SQL Exception while setting security context. Error was: \n", e.getMessage());
				e.printStackTrace();
			}
		}
		return request;
	}

	private static class SimplePrincipal implements Principal {
		private String name;

		public SimplePrincipal(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	private Set<String> getRoles(OAuthToken accessToken) throws SQLException {
		try {
			Statement st = conn.createStatement();
			final ResultSet rs = st.executeQuery("SELECT r.name AS role_name FROM users INNER JOIN `access_tokens` AS act ON act.`user_id` = users.`id` INNER JOIN users_roles AS ur ON ur.`user_id` = users.`id` INNER JOIN roles AS r ON r.`id` = ur.`role_id` WHERE token = '"+accessToken.getToken() +"'");
			final Set<String> roles = new HashSet<String>();
			
			while (rs.next()) {
				roles.add(rs.getString("role_name"));
			}
			
			return roles;
		} catch (SQLException e) {
			logger.error("SQL Exception while setting security context. Error was: \n", e.getCause());
			throw new SQLException("User {} may not be assigned to any role");
		}
	}
	
	private String getUserName(OAuthToken accessToken) throws SQLException {
		try {
			Statement st = conn.createStatement();
			final ResultSet rs = st.executeQuery("SELECT users.name AS user_name FROM users INNER JOIN `access_tokens` AS act ON act.`user_id` = users.`id` WHERE token = '"+accessToken.getToken() +"'");
			if (rs.next()) {
				return rs.getString("user_name");
			}
			throw new SQLException();
		} catch (SQLException e) {
			logger.error("SQL Exception while getting user's name form access token {}. Error was: \n", accessToken.getToken(), e.getCause());
			throw new SQLException("User not found or access token not assigned to a user");
		}
	}
}
