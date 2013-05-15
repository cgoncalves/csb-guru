package pt.it.av.atnog.csb.auth.oauth;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.resteasy.auth.oauth.OAuthConsumer;
import org.jboss.resteasy.auth.oauth.OAuthException;
import org.jboss.resteasy.auth.oauth.OAuthRequestToken;
import org.jboss.resteasy.auth.oauth.OAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CSB OAuth Provider that keeps all data in DB.
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 * 
 */
public class CsbOAuthProviderImpl implements CsbOAuthProvider {

	private String realm;
	private static Connection conn;
	private static final String DEFAULT_CONSUMER_ROLE = "user";
	private static final String DB_DRIVER;
	private static final String DB_URL;
	private static final String DB_USERNAME;
	private static final String DB_PASSWORD;
	private static final Logger logger = LoggerFactory.getLogger(CsbOAuthProviderImpl.class);

	static {
		Properties props = new Properties();
		try {
			props.load(CsbOAuthProviderImpl.class.getResourceAsStream("/db.properties"));
		} catch (Exception ex) {
			throw new RuntimeException("db.properties resource is not available");
		}
		
		DB_DRIVER = props.getProperty("db.driver");
		DB_URL = props.getProperty("db.url");
		DB_USERNAME = props.getProperty("db.username");
		DB_PASSWORD = props.getProperty("db.password");
	}
	
	public CsbOAuthProviderImpl() {
		this("default");
	}

	public CsbOAuthProviderImpl(String realm) {

		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch (Exception ex) {
			throw new RuntimeException("In memory OAuth DB can not be created " + ex.getMessage());
		}

		this.realm = realm;
	}

	public String authoriseRequestToken(String consumerKey, String requestToken) throws OAuthException {
		try {
			String verifier = makeRandomString();
			update("UPDATE request_tokens SET verifier='" + verifier + "' " + "WHERE token='" + requestToken + "'");

			return verifier;
		} catch (SQLException ex) {
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Request token for the consumer with key " + consumerKey
					+ " can not be authorized");
		}
	}
	
	public String authoriseRequestToken(String consumerKey, String requestToken, String username) throws OAuthException {
		logger.debug("User {} is giving authorization to consumer key {} with request token {}", new Object[] {username, consumerKey, requestToken});

		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT id FROM users WHERE name = '" + username + "'");
			if (rs.next()) {
				update("UPDATE request_tokens SET user_id='" + rs.getInt("id") + "' " + "WHERE token='" + requestToken + "'");
				String verifier = authoriseRequestToken(consumerKey, requestToken);
				return verifier;
			}
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Request token for the consumer with key " + consumerKey
					+ " can not be authorized");
		} catch (SQLException ex) {
			logger.debug("SQL Exception while authorizing request token ({})", ex.getMessage());
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Request token for the consumer with key " + consumerKey
					+ " can not be authorized");
		}
	}

	public void checkTimestamp(OAuthToken token, long timestamp) throws OAuthException {
		// TODO Auto-generated method stub

	}

	public OAuthToken getAccessToken(String consumerKey, String accessToken) throws OAuthException {
		try {
			Statement st = conn.createStatement();
			logger.debug("Getting OAuthToken from consumer key {} with access token {}", new Object[] {consumerKey, accessToken});
			ResultSet rs = st.executeQuery("SELECT * FROM access_tokens WHERE" + " token = '" + accessToken + "'");
			if (rs.next()) {
				String token = rs.getString("token");
				String secret = rs.getString("secret");
				// String scopes = rs.getString("scopes");
				// String permissions = rs.getString("permissions");
				String tokenConsumerKey = rs.getString("consumer_key");

				if (consumerKey != null && !tokenConsumerKey.equals(consumerKey)) {
					logger.error("6");
					throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
				}

				// return new OAuthToken(token, secret, scopes == null ? null :
				// new String[] { scopes }, permissions == null ? null
				// : new String[] { permissions }, -1,
				// getConsumer(tokenConsumerKey));
				return new OAuthToken(token, secret, null, null, -1, getConsumer(tokenConsumerKey));
			} else {
				logger.error("7");
				throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
			}
		} catch (SQLException ex) {
			logger.error("8");
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
		}
	}

	public OAuthConsumer getConsumer(String consumerKey) throws OAuthException {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM consumers WHERE `key` = '" + consumerKey + "'");
			if (rs.next()) {
				String key = rs.getString("key");
				String secret = rs.getString("secret");
				String displayName = rs.getString("display_name");
				String connectURI = rs.getString("connect_uri");
				// String scopes = rs.getString("scopes");
				// String perms = rs.getString("permissions");
				// OAuthConsumer consumer = new OAuthConsumer(key, secret,
				// displayName, connectURI, perms != null ? new String[] { perms
				// }
				// : null);
				OAuthConsumer consumer = new OAuthConsumer(key, secret, displayName, connectURI, null);
				// consumer.setScopes(new String[] { scopes });
				return consumer;
			} else {
				logger.error("1");
				throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
			}
		} catch (SQLException ex) {
			logger.error("2");
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
		}
	}

	public String getRealm() {
		return realm;
	}

	public OAuthRequestToken getRequestToken(String consumerKey, String requestToken) throws OAuthException {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM request_tokens WHERE" + " token = '" + requestToken + "'");
			if (rs.next()) {
				String token = rs.getString("token");
				String secret = rs.getString("secret");
				String callback = rs.getString("callback");
				// String scopes = rs.getString("scopes");
				// String permissions = rs.getString("permissions");
				String verifier = rs.getString("verifier");
				String tokenConsumerKey = rs.getString("consumer_key");

				if (consumerKey != null && !tokenConsumerKey.equals(consumerKey)) {
					logger.error("3");
					throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
				}

				// OAuthRequestToken newToken = new OAuthRequestToken(token,
				// secret, callback,
				// scopes == null ? null : new String[] { scopes }, permissions
				// == null ? null : new String[] { permissions }, -1,
				// getConsumer(tokenConsumerKey));
				OAuthRequestToken newToken = new OAuthRequestToken(token, secret, callback, null, null, -1, getConsumer(tokenConsumerKey));
				newToken.setVerifier(verifier);
				return newToken;
			} else {
				logger.error("4");
				throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
			}
		} catch (SQLException ex) {
			logger.error("5");
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "No such consumer key " + consumerKey);
		}
	}

	public OAuthToken makeAccessToken(String consumerKey, String requestTokenKey, String verifier) throws OAuthException {
		OAuthRequestToken requestToken = checkVerifier(consumerKey, requestTokenKey, verifier);
		try {
			String token = makeRandomString();
			String secret = makeRandomString();
			// String[] scopes = requestToken.getScopes();
			// String[] permissions = requestToken.getPermissions();
			// update("INSERT INTO access_tokens(token,key,secret,scopes,permissions) "
			// + "VALUES('" + token + "', '" + consumerKey + "', '"
			// + secret + "'" + ", " + (scopes != null ? "'" + scopes[0] + "'" :
			// null) + ", "
			// + (permissions != null ? "'" + permissions[0] + "'" : null) +
			// ")");
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT user_id FROM request_tokens WHERE token = '" + requestTokenKey + "'");
			if (!rs.next()) {
				logger.error("No user_id associated to the requested token");
				throw new SQLException();
			}
			
			// delete request token
			removeRequestToken(requestTokenKey);
			
			// delete previous access tokens
			update("DELETE FROM access_tokens WHERE consumer_key = '" + consumerKey + "' and user_id = '" + rs.getString("user_id") + "'");
			update("INSERT INTO access_tokens(token, consumer_key, secret, user_id) " + "VALUES('" + token + "', '" + consumerKey + "', '"
					+ secret + "', '" + rs.getString("user_id") + "')");
			return new OAuthToken(token, secret, requestToken.getScopes(), requestToken.getPermissions(), -1, requestToken.getConsumer());
		} catch (SQLException ex) {
			logger.debug("SQL Exception while trading request token {} with verifier {} ({})", new Object[] {requestTokenKey, verifier, ex.getMessage()});
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Request token for the consumer with key " + consumerKey
					+ " can not be created");
		}
	}

	private OAuthRequestToken checkVerifier(String consumerKey, String requestToken, String verifier) throws OAuthException {
		OAuthRequestToken token = getRequestToken(consumerKey, requestToken);
		checkCustomerKey(token, consumerKey);
		// check the verifier, which is only set when the request token was accepted
		if (verifier == null || !verifier.equals(token.getVerifier()))
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Invalid verifier code for token " + requestToken);
		return token;
	}
	
	private void removeRequestToken(String requestToken) {
		try {
			update("DELETE FROM request_tokens WHERE token='" + requestToken + "'");
		} catch (SQLException ex) {
			throw new RuntimeException("Request Token can not be deleted");
		}
	}

	public OAuthToken makeRequestToken(String consumerKey, String callback, String[] scopes, String[] permissions) throws OAuthException {
		try {
			String token = makeRandomString();
			String secret = makeRandomString();
//			update("INSERT INTO request_tokens(token,key,secret,callback,scopes,permissions) " + "VALUES('" + token + "', '" + consumerKey
//					+ "', '" + secret + "', '" + callback + "'" + ", " + (scopes != null ? "'" + scopes[0] + "'" : null) + ", "
//					+ (permissions != null ? "'" + permissions[0] + "'" : null) + ")");
			update("INSERT INTO request_tokens(token,consumer_key,secret,callback) " + "VALUES('" + token + "', '" + consumerKey
					+ "', '" + secret + "', '" + callback + "')");

			return new OAuthRequestToken(token, secret, callback, scopes, permissions, -1, getConsumer(consumerKey));
		} catch (SQLException ex) {
			logger.error("Request token for the consumer with key {} can not be created due to a SQL Exception: {}", consumerKey, ex.getMessage());
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Request token for the consumer with key " + consumerKey
					+ " can not be created");
		}

	}

	public OAuthConsumer registerConsumer(String consumerKey, String displayName, String connectURI) throws OAuthException {

		String secret = makeRandomString();

		try {
			update("INSERT INTO consumers(`key`,secret,display_name,connect_uri) " + "VALUES('" + consumerKey + "', '" + secret + "'"
					+ ", " + (displayName == null ? null : "'" + displayName + "'") + ", "
					+ (connectURI == null ? null : "'" + connectURI + "'") + ")");

		} catch (SQLException ex) {
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Consumer with key " + consumerKey + " can not be created");
		}
		return new OAuthConsumer(consumerKey, secret, displayName, connectURI);
	}

	private static String makeRandomString() {
		return RandomStringUtils.randomAlphanumeric(120);
		// return UUID.randomUUID().toString();
	}

	private void checkCustomerKey(OAuthToken token, String customerKey) throws OAuthException {
		if (customerKey != null && !customerKey.equals(token.getConsumer().getKey())) {
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Invalid customer key");
		}
	}

	public void registerConsumerScopes(String consumerKey, String[] scopes) throws OAuthException {
		try {
			if (scopes != null) {
				update("UPDATE consumers SET scopes=" + "'" + scopes[0] + "'" + " WHERE `key`='" + consumerKey + "'");
			}
		} catch (SQLException ex) {
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Scopes for the consumer with key " + consumerKey
					+ " can not be registered");
		}

	}

	public void registerConsumerPermissions(String consumerKey, String[] permissions) throws OAuthException {
		try {
			if (permissions != null) {
				update("UPDATE consumers SET permissions=" + "'" + permissions[0] + "'" + " WHERE `key`='" + consumerKey + "'");
			}
		} catch (SQLException ex) {
			throw new OAuthException(HttpURLConnection.HTTP_UNAUTHORIZED, "Scopes for the consumer with key " + consumerKey
					+ " can not be registered");
		}

	}

	public Set<String> convertPermissionsToRoles(String[] permissions) {
		Set<String> roles = new HashSet<String>();
		roles.add(DEFAULT_CONSUMER_ROLE); // FIXME
		if (permissions == null || permissions.length == 0) {
			return roles;
		}
		StringBuilder query = new StringBuilder();
		query.append("SELECT role FROM permissions WHERE ");
		for (int i = 0; i < permissions.length; i++) {
			query.append("permission='" + permissions[i] + "'");
			if (i + 1 < permissions.length) {
				query.append(" OR ");
			}
		}
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query.toString());
			if (rs.next()) {
				String rolesValues = rs.getString("role");
				roles.add(rolesValues);
			}
		} catch (SQLException ex) {
			throw new RuntimeException("No role exists for permission " + permissions);
		}
		return roles;
	}

	private static synchronized void update(String expression) throws SQLException {

		Statement st = conn.createStatement(); // statements
		int i = st.executeUpdate(expression); // run the query

		if (i == -1) {
			System.out.println("db error : " + expression);
		}

		st.close();
	}

	@Override
	public boolean hasUserGivenConsumerAccess(OAuthConsumer consumer, String name) throws OAuthException {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT access_tokens.id FROM access_tokens INNER JOIN users ON users.id = access_tokens.user_id WHERE consumer_key = '" + consumer.getKey() + "' and users.name = '" + name + "'");
			return rs.next();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new OAuthException(HttpURLConnection.HTTP_INTERNAL_ERROR, "Failed checking if user has given consumer's access");
		}
	}
}
