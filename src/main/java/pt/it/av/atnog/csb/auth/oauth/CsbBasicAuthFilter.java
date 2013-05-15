package pt.it.av.atnog.csb.auth.oauth;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.it.av.atnog.csb.entity.csb.Role;
import pt.it.av.atnog.csb.entity.csb.User;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public class CsbBasicAuthFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(CsbBasicAuthFilter.class);

	@PersistenceContext(unitName = "CSBPU")
	private EntityManager em;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		_doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
	}

	@Override
	public void destroy() {
	}

	private void _doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {
		String header = request.getHeader("Authorization");
		try {
			if (header != null && header.startsWith("Basic")) {
				String base64Value = header.substring(6);
				Base64 base64 = new Base64();
				String decoded = new String(base64.decode(base64Value.getBytes()));
				String[] pair = decoded.split(":");
				String username = pair[0];
				String password = pair[1];

				request = createSecurityContext(request, response, username, password);
				filterChain.doFilter(request, response);
			} else {
				response.setStatus(HttpStatus.SC_UNAUTHORIZED);
		        response.setHeader("WWW-Authenticate", "basic realm=\"Auth\"");
			}
		} catch (UnauthorizedException e) {
			logger.debug("User is unauthorized to access the requested service");
			response.setStatus(HttpStatus.SC_UNAUTHORIZED);
	        response.setHeader("WWW-Authenticate", "basic realm=\"Auth\"");
		} catch (IOException e) {
			logger.debug("Request failed ({})", e.getMessage());
		}
	}

	private HttpServletRequest createSecurityContext(HttpServletRequest request, HttpServletResponse response, String username,
			String password) throws IOException, UnauthorizedException {
		try {
			User user = (User) em.createNamedQuery("User.findByName").setParameter("name", username).getSingleResult();
			String sha256Password = DigestUtils.sha256Hex(password);
	
			if (!user.getPassword().equals(sha256Password)) {
				logger.debug("User {} authentication failed (password does not match)", username);
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User and/or password unexpected");
			}
	
			logger.debug("User {} successfully authenticated", username);
			final Principal principal = new SimplePrincipal(username);
			final Set<String> roles = getRoles(user);
			logger.trace("User {} is enrolled in the following roles: {}", username, roles);
	
			return new HttpServletRequestWrapper(request) {
				@Override
				public Principal getUserPrincipal() {
					return principal;
				}
	
				@Override
				public boolean isUserInRole(String role) {
					return roles.contains(role);
				}
	
				@Override
				public String getAuthType() {
					return BASIC_AUTH;
				}
			};
		} catch(NoResultException e) {
			throw new UnauthorizedException();
		}
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

	private Set<String> getRoles(User user) {
		final Set<String> roles = new HashSet<String>();
		for (Role role : user.getRoles()) {
			roles.add(role.getName());
		}

		return roles;
	}

}
