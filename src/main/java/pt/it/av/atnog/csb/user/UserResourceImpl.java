package pt.it.av.atnog.csb.user;

import java.util.Arrays;
import java.util.HashSet;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.codec.digest.DigestUtils;

import pt.it.av.atnog.csb.acm.ACM2Impl;
import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.csb.Role;
import pt.it.av.atnog.csb.entity.csb.User;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Stateless
public class UserResourceImpl implements UserResource {

	@PersistenceContext(unitName = "CSBPU")
	private EntityManager em;
	
	private static final String DEFAULT_ROLE = "user";

	@Override
	public User createUser(String name, String password, String email) {
		final Role role = em.createNamedQuery("Role.findByName", Role.class).setParameter("name", DEFAULT_ROLE).getSingleResult();
		String sha256Password = DigestUtils.sha256Hex(password);
		User user = new User(name, sha256Password, email, new HashSet<Role>(Arrays.asList(role)));
		ACM2Impl.getInstance().createUser(name, name, email, password);
		try {
			em.persist(user);
		} catch (Exception e) {
			throw new CSBException(Status.CONFLICT, "User already exists");
		}
		return user;
	}

	@Override
	public void deleteUser(String id) {
		try {
			User user = em.createNamedQuery("User.findById", User.class).setParameter("id", id).getSingleResult();
			em.remove(user);
		} catch (NoResultException e) {
			throw new CSBException(Status.NOT_FOUND, "User not found");
		}
	}

	public UserResourceImpl() {
	}

	public UserResourceImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public User getUser(SecurityContext ctx, String name) {
		if (ctx.isUserInRole("admin") || ctx.getUserPrincipal().getName().equals(name)) {
			try {
				return em.createNamedQuery("User.findByName", User.class).setParameter("name", name).getSingleResult();
			} catch (NoResultException e) {
				throw new CSBException(Status.NOT_FOUND, "User '" + name + "' not found.");
			}
		} else {
			throw new CSBException(Status.FORBIDDEN, "Access forbidden for the requested user");
		}
	}

	@Override
	public User getUser(SecurityContext ctx) {
		return getUser(ctx, ctx.getUserPrincipal().getName());
	}
}
