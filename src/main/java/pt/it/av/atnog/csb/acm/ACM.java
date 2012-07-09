package pt.it.av.atnog.csb.acm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileExistsException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import pt.it.av.atnog.csb.entity.csb.ACMLog;
import pt.it.av.atnog.csb.entity.csb.App;
import pt.it.av.atnog.csb.entity.csb.Manifest;

/**
 * Application Control Manager
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public interface ACM {

	/**
	 * Get a "GitPorcelain"-like API to interact with the Git repository.
	 * 
	 * @return a Git repository
	 */
	Git getGit();

	/**
	 * Initialize a new repository.
	 * 
	 * @throws FileExistsException if the wanted repository already exists
	 * @throws IOException if an IO operation has failed while initializing the new repository
	 */
	void init() throws FileExistsException, IOException;
	
	/**
	 * Initialize a new repository adding and committing the given {@link Manifest}.
	 * 
	 * @param manifest the {@link Manifest} to include in the repository
	 * @throws FileExistsException if the wanted repository already exists
	 * @throws IOException if an IO operation has failed while initializing the new repository
	 * @throws JAXBException if marshalling the {@link Manifest} fails
	 */
	void init(Manifest manifest) throws FileExistsException, IOException, JAXBException;

	/**
	 * Get the remote repository URI (eg. git://user@host/project).
	 * 
	 * @return a {@link String} representing the remote repository URI
	 */
	String getRemoteRepository();

	/**
	 * Get the Git repository this class is interacting with.
	 * 
	 * @return the Git repository this class is interacting with
	 */
	Repository getRepository();

	/**
	 * Get the last commit (the latest in HEAD).
	 * 
	 * @return the latest 
	 * @throws AmbiguousObjectException
	 * @throws IOException
	 */
	RevCommit getLastCommit() throws AmbiguousObjectException, IOException;

	/**
	 * Get the Git directory path.
	 * 
	 * @return the Git directory path
	 */
	String getGitDirPath();

	/**
	 * Checks if {@link App} exists in ACM.
	 * 
	 * @return true if {@link App} exists
	 */
	boolean existsApp();

	/**
	 * Get the {@link Manifest}.
	 * 
	 * @return the {@link Manifest}
	 * @throws FileNotFoundException if no {@link Manifest} was found
	 */
	Manifest getManifest() throws FileNotFoundException;

	/**
	 * Delete an {@link App} from ACM.
	 * 
	 * @param appId the {@link App} id
	 * @throws IOException if an IO operation has failed (eg. deleting the app directory)
	 */
	void deleteApp(String appId) throws IOException;

	/**
	 * Archive and compress a {@link App} to .zip format. 
	 * 
	 * @return the data archived and compressed
	 * @throws IOException
	 */
	InputStream getData() throws IOException;
	
	/**
	 * Get the committing log of an {@link App}.
	 * 
	 * @return the log
	 */
	ACMLog getLog();
}
