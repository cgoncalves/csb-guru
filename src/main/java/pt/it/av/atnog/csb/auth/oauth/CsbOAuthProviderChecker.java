package pt.it.av.atnog.csb.auth.oauth;

import java.util.Set;

import org.jboss.resteasy.auth.oauth.OAuthConsumer;
import org.jboss.resteasy.auth.oauth.OAuthException;
import org.jboss.resteasy.auth.oauth.OAuthRequestToken;
import org.jboss.resteasy.auth.oauth.OAuthToken;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public class CsbOAuthProviderChecker implements CsbOAuthProvider {

	private CsbOAuthProvider provider;

	public CsbOAuthProviderChecker(CsbOAuthProvider provider) {
		this.provider = provider;
	}

	private <T> T checkNull(T arg) {
		if (arg == null)
			throw new RuntimeException("CsbOAuthProvider should not return null");
		return arg;
	}

	public OAuthConsumer registerConsumer(String consumerKey, String displayName, String connectURI) throws OAuthException {
		return checkNull(provider.registerConsumer(consumerKey, displayName, connectURI));
	}

	public OAuthConsumer getConsumer(String consumerKey) throws OAuthException {
		return checkNull(provider.getConsumer(consumerKey));
	}

	public String getRealm() {
		return checkNull(provider.getRealm());
	}

	public OAuthRequestToken getRequestToken(String consumerKey, String requestKey) throws OAuthException {
		return checkNull(provider.getRequestToken(consumerKey, requestKey));
	}

	public OAuthToken getAccessToken(String consumerKey, String accessKey) throws OAuthException {
		return checkNull(provider.getAccessToken(consumerKey, accessKey));
	}

	public void checkTimestamp(OAuthToken token, long timestamp) throws OAuthException {
		provider.checkTimestamp(token, timestamp);
	}

	public OAuthToken makeAccessToken(String consumerKey, String requestKey, String verifier) throws OAuthException {
		return checkNull(provider.makeAccessToken(consumerKey, requestKey, verifier));
	}

	public OAuthToken makeRequestToken(String consumerKey, String callback, String[] scopes, String[] permissions) throws OAuthException {
		return checkNull(provider.makeRequestToken(consumerKey, callback, scopes, permissions));
	}

	public String authoriseRequestToken(String consumerKey, String requestKey) throws OAuthException {
		return checkNull(provider.authoriseRequestToken(consumerKey, requestKey));
	}

	public String authoriseRequestToken(String consumerKey, String requestKey, String username) throws OAuthException {
		return checkNull(provider.authoriseRequestToken(consumerKey, requestKey, username));
	}

	public void registerConsumerScopes(String consumerKey, String[] scopes) throws OAuthException {
		provider.registerConsumerScopes(consumerKey, scopes);
	}

	public void registerConsumerPermissions(String consumerKey, String[] permissions) throws OAuthException {
		provider.registerConsumerPermissions(consumerKey, permissions);
	}

	public Set<String> convertPermissionsToRoles(String[] permissions) {
		return provider.convertPermissionsToRoles(permissions);
	}

	public boolean hasUserGivenConsumerAccess(OAuthConsumer consumer, String name) throws OAuthException {
		return provider.hasUserGivenConsumerAccess(consumer, name);
	}
}
