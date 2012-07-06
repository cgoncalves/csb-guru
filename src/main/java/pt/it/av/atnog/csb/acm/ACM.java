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
import pt.it.av.atnog.csb.entity.csb.Manifest;

/**
 * Application Control Manager
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public interface ACM {

	Git getGit();

	void init() throws FileExistsException, IOException;
	
	void init(Manifest manifest) throws FileExistsException, IOException, JAXBException;

	String getRemoteRepository();

	Repository getRepository();

	RevCommit getLastCommit() throws AmbiguousObjectException, IOException;

	String getGitDirPath();

	boolean existsApp();

	Manifest getManifest() throws FileNotFoundException;

	void deleteApp(String appId) throws IOException;

	/**
	 * Archive. Compress to .zip format with the highest level of compression.
	 * Even though it will be slowest, we expect to gain in time overall when
	 * uploading it to the PM.
	 */
	InputStream getData() throws IOException;
	
	ACMLog getLog();
}
