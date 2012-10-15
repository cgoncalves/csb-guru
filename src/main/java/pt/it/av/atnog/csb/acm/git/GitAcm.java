package pt.it.av.atnog.csb.acm.git;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;

import pt.it.av.atnog.csb.acm.ACM;
import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.csb.ACMCommit;
import pt.it.av.atnog.csb.entity.csb.ACMLog;
import pt.it.av.atnog.csb.entity.csb.Manifest;

/**
 * Git Application Control Manager
 * 
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public class GitAcm implements ACM {

	private static final String ACM_HOOK_PRE_RECEIVE_PATH = "/acm/post-receive"; // TODO load from properties file
	private static final String ACM_PATH = "/acm"; // TODO load from properties file
	private static final String MANIFEST_FILE_NAME = "MANIFEST.MF"; // TODO load from properties file
	public static final String ACM_REPOSITORY_PARENT_PATH = "ssh://cgoncalves@fog.av.it.pt" + ACM_PATH; // FIXME load from properties file

	private final Git git;
	private final String appId;

	public GitAcm(File repoDir) throws IOException {
		Repository repo = new FileRepository(repoDir);
		this.git = new Git(repo);
		this.appId = repoDir.getName();
	}

	public GitAcm(String appId) throws IOException {
		this(new File(getInternalGitDirPath(appId)));
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public RevCommit getLastCommit() throws AmbiguousObjectException, IOException {
		ObjectId lastCommitId = getRepository().resolve(Constants.HEAD);
		RevWalk revWalk = new RevWalk(getRepository());
		return revWalk.parseCommit(lastCommitId);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Git getGit() {
		return git;
	}

	/**
	 * @inheritDoc
	 */
	public Repository getRepository() {
		return git.getRepository();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void init() throws IOException {
		File directory = new File(getInternalGitDirPath(appId));

		if (directory.exists()) {
			throw new FileExistsException("Repository '" + appId + "' already exists");
		}

		// check first if pre-receive hook file to copy exists and is readable
		File originalPreHook = new File(ACM_HOOK_PRE_RECEIVE_PATH);

		if (!originalPreHook.exists() || !originalPreHook.isFile() || !originalPreHook.canRead()) {
			throw new IOException("Couldn't open hook " + ACM_HOOK_PRE_RECEIVE_PATH);
		}

		// create the new Git repository directory
		if (!directory.mkdir()) {
			throw new IOException("Couldn't create directory " + directory.getAbsolutePath());
		}

		// init Git repository
		InitCommand initCommand = Git.init();
		initCommand.setBare(true);
		initCommand.setDirectory(directory);
		initCommand.call();

		// copy pre-receive hook file and set execute permission to the file
		// owner
		File preReceiveHook = new File(directory, "hooks/post-receive");

		/*
		 * TODO instead of copying the hook file, it'd be better if we could
		 * symlink it instead but due to Java constrains on trying to be
		 * cross-operating system compatible, Java does not offer a method to
		 * create a symbolic link because that concept only exists on Unix OS.
		 */
		FileUtils.copyFile(originalPreHook, preReceiveHook);
		preReceiveHook.setExecutable(true);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
    public void init(Manifest manifest) throws FileExistsException, IOException, JAXBException {
		File directory = new File(getInternalGitDirPath(appId));
		File directoryWorkingTree = new File(getInternalGitDirPath("wt-"+appId));

		if (directory.exists() || directoryWorkingTree.exists()) {
			throw new FileExistsException("Repository '" + appId + "' already exists");
		}

		// check first if pre-receive hook file to copy exists and is readable
		File originalPreHook = new File(ACM_HOOK_PRE_RECEIVE_PATH);

		if (!originalPreHook.exists() || !originalPreHook.isFile() || !originalPreHook.canRead()) {
			throw new IOException("Couldn't open hook " + ACM_HOOK_PRE_RECEIVE_PATH);
		}

		// create the new Git repository directory
		if (!directory.mkdir() || !directoryWorkingTree.mkdir()) {
			throw new IOException("Couldn't create directory " + directory.getAbsolutePath());
		}
		
		File manifestWorkingTree = new File(directoryWorkingTree, MANIFEST_FILE_NAME);
		JAXBContext context = JAXBContext.newInstance(Manifest.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(manifest, manifestWorkingTree);
		
		// init Git repository (temp directory as it will be a working tree repository used to ease the add of the manifest file)
		InitCommand initCommand = Git.init();
		initCommand.setBare(false);
		initCommand.setDirectory(directoryWorkingTree);
		Git git_wt = initCommand.call();

		try {
			AddCommand addCommand = git_wt.add();
			addCommand.addFilepattern(MANIFEST_FILE_NAME);
			addCommand.call();
			
			CommitCommand commitCommand = git_wt.commit();
			commitCommand.setMessage("Added " + MANIFEST_FILE_NAME);
	        commitCommand.call();
        } catch (NoHeadException e) {
	        e.printStackTrace();
        } catch (NoMessageException e) {
	        e.printStackTrace();
        } catch (ConcurrentRefUpdateException e) {
	        e.printStackTrace();
        } catch (JGitInternalException e) {
	        e.printStackTrace();
        } catch (WrongRepositoryStateException e) {
	        e.printStackTrace();
        } catch (NoFilepatternException e) {
	        e.printStackTrace();
        }
		
		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setBare(true);
		cloneCommand.setDirectory(directory);
		cloneCommand.setURI(directoryWorkingTree.getAbsolutePath());
		cloneCommand.setCloneAllBranches(true);
		cloneCommand.setNoCheckout(false);
		cloneCommand.call();
		
		// copy pre-receive hook file and set execute permission to the file owner
		File preHook = new File(directory, "hooks/post-receive");

		/*
		 * TODO instead of copying the hook file, it'd be better if we could
		 * symlink it instead but due to Java constrains on trying to be
		 * cross-operating system compatible, Java does not offer a method to
		 * create a symbolic link because that concept only exists on Unix OS.
		 */
		FileUtils.copyFile(originalPreHook, preHook);
		preHook.setExecutable(true, false);
		
		FileUtils.deleteDirectory(directoryWorkingTree);
		directory.setWritable(true, true);
    }

	/**
	 * @inheritDoc
	 */
	@Override
	public String getGitDirPath() {
		return ACM_PATH + "/" + appId;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getRemoteRepository() {
		return getRemoteRepository(appId);
	}
	
	public static String getRemoteRepository(String appId) {
		return ACM_REPOSITORY_PARENT_PATH + "/" + appId;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean existsApp() {
		File repoDir = new File(getGitDirPath());
		return (repoDir.exists() && repoDir.isDirectory());
	}

	private static String getInternalGitDirPath(String appId) {
		return ACM_PATH + "/" + appId;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Manifest getManifest() throws FileNotFoundException {
		ProcessBuilder pb = new ProcessBuilder("git", "cat-file", "blob", "master:"+MANIFEST_FILE_NAME);
		pb.directory(new File(getGitDirPath()));
		Process p;

		try {
			p = pb.start();
			
			JAXBContext jc = JAXBContext.newInstance(Manifest.class);
			Unmarshaller u = jc.createUnmarshaller();
			return (Manifest) u.unmarshal(p.getInputStream());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (IOException e) {
	        e.printStackTrace();
        }

		throw new FileNotFoundException();
	}

	/**
	 * @inheritDoc
	 */
	@Override
    public void deleteApp(String appId) throws IOException {
		File dir = new File(getGitDirPath());
	    FileUtils.deleteDirectory(dir);
    }

	/**
	 * @inheritDoc
	 */
	@Override
    public InputStream getData() throws IOException {
		ProcessBuilder pb = new ProcessBuilder("git", "archive", "--remote", getGitDirPath(), "--format=zip", "-9", "master");
		Process p = pb.start();
	    return p.getInputStream();
    }

	/**
	 * @inheritDoc
	 */
	@Override
    public ACMLog getLog() {
		try {
			ACMCommit commit;
			ACMLog log = new ACMLog();
			Iterable<RevCommit> it = git.log().call();
			List<ACMCommit> commits = log.getACMCommits();
			
			for (RevCommit c : it) {
				commit = new ACMCommit();
				commit.setAuthorEmail(c.getAuthorIdent().getEmailAddress());
				commit.setAuthorName(c.getAuthorIdent().getName());
				commit.setMessage(c.getFullMessage());
				commit.setRevision(ObjectId.toString(c.getId()));
				commits.add(commit);
			}
			
			return log;
		} catch (NoHeadException e) {
			e.printStackTrace();
			throw new CSBException(Status.INTERNAL_SERVER_ERROR, "Internal server error while getting the log.");
		}
    }
}
