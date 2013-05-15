package pt.it.av.atnog.csb.acm;

import java.io.File;
import java.io.FileNotFoundException;

import pt.it.av.atnog.csb.entity.csb.Manifest;
import sonia.scm.group.Group;
import sonia.scm.repository.Repository;
import sonia.scm.user.User;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
public interface ACM2 {
	
	// Repository
	Repository getRepository(String name, String repositoryType);
	void createRepository(Repository repository);
	void createRepository(Repository repository, Manifest manifest);
	void createRepository(String name, String type);
	void deleteRepository(Repository repository);
	void deleteRepository(String name);
	File zipRepository(Repository repository);
	String getUrl(Repository repository);
	String getUrl(String name, String type);
	
	// Group
	void createGroup(Group group);
	void createGroup(String name);
	void deleteGroup(Group group);
	void deleteGroup(String name);
	void updateGroup(Group group);
	
	// User
	void createUser(User user);
	void createUser(String name, String displayName, String email, String password);
	void deleteUser(User user);
	void deleteUser(String name);
	void updateUser(User user);
	
	// Permissions
	void addGroupMember(Group group, String user);
	void addGroupMember(String group, String user);
	
	// Misc
	void addManifest(Repository repository, Manifest manifest);
	Manifest getManifest(Repository repository) throws FileNotFoundException;
}
