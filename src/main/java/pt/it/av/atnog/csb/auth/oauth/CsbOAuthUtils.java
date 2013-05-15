package pt.it.av.atnog.csb.auth.oauth;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.jboss.resteasy.auth.oauth.OAuthUtils;
import org.jboss.resteasy.logging.Logger;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
public class CsbOAuthUtils extends OAuthUtils {
	
	/**
	 * Name of the OAuthProvider Servlet Context Attribute name.
	 */
	private static final String ATTR_OAUTH_PROVIDER = CsbOAuthProvider.class.getName();
	
	private final static Logger logger = Logger.getLogger(CsbOAuthUtils.class);

	/**
	 * Loads the OAuthProvider as specified in the Servlet Context parameters, and caches it in the Servlet Context attributes for reuse.
	 */
	public static CsbOAuthProvider getOAuthProvider(ServletContext context) throws ServletException {
		CsbOAuthProvider provider = (CsbOAuthProvider) context.getAttribute(ATTR_OAUTH_PROVIDER);
		if(provider != null)
			return provider;
		
		String providerClassName = context.getInitParameter(CsbOAuthServlet.PARAM_PROVIDER_CLASS);
		if(providerClassName == null)
			throw new ServletException(CsbOAuthServlet.PARAM_PROVIDER_CLASS+" parameter required");
		try {
			logger.info("Loading CsbOAuthProvider: "+ providerClassName);
			Class<?> providerClass = Class.forName(providerClassName);
			if(!CsbOAuthProvider.class.isAssignableFrom(providerClass))
				throw new ServletException(CsbOAuthServlet.PARAM_PROVIDER_CLASS+" class "+providerClassName+" must be an instance of CsbOAuthProvider");
			provider = new CsbOAuthProviderChecker((CsbOAuthProvider) providerClass.newInstance());
			context.setAttribute(ATTR_OAUTH_PROVIDER, provider);
			return provider;
		} catch (ClassNotFoundException e) {
			throw new ServletException(CsbOAuthServlet.PARAM_PROVIDER_CLASS+" class "+providerClassName+" not found");
		} catch (Exception e) {
			throw new ServletException(CsbOAuthServlet.PARAM_PROVIDER_CLASS+" class "+providerClassName+" could not be instanciated", e);
		}
	}
}
