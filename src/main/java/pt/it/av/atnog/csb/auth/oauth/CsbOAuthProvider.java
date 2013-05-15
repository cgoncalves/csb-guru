package pt.it.av.atnog.csb.auth.oauth;

import org.jboss.resteasy.auth.oauth.OAuthConsumer;
import org.jboss.resteasy.auth.oauth.OAuthException;
import org.jboss.resteasy.auth.oauth.OAuthProvider;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
public interface CsbOAuthProvider extends OAuthProvider {

	public String authoriseRequestToken(String consumerKey, String requestToken, String username) throws OAuthException;

	public boolean hasUserGivenConsumerAccess(OAuthConsumer consumer, String name) throws OAuthException;
}
