package pt.it.av.atnog.csb.acm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.manager.BasicScmManager;
import org.apache.maven.scm.manager.NoSuchScmProviderException;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.apache.maven.scm.provider.svn.svnexe.SvnExeScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.it.av.atnog.csb.entity.csb.Manifest;
import pt.it.av.atnog.csb.util.ZipUtils;
import sonia.scm.client.RepositoryClientHandler;
import sonia.scm.client.ScmClient;
import sonia.scm.client.ScmClientSession;
import sonia.scm.group.Group;
import sonia.scm.repository.Repository;
import sonia.scm.user.User;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
public class ACM2Impl implements ACM2 {

	public static final String ACM2_URL;
	private static final String ACM2_SCM_USERNAME;
	private static final String ACM2_SCM_PASSWORD;
	private static final String ACM2_CSB_USERNAME;
	private static final String ACM2_CSB_PASSWORD;
	private static final String ACM2_MANIFEST;
	private static ACM2Impl instance = null;
	private ScmManager scmManager;
	private static final Logger logger = LoggerFactory.getLogger(ACM2Impl.class);

	static {
		Properties props = new Properties();
		try {
			props.load(ACM2Impl.class.getResourceAsStream("/acm2.properties"));
		} catch (Exception ex) {
			throw new RuntimeException("acm2.properties resource is not available");
		}

		ACM2_URL = props.getProperty("acm2.url");
		ACM2_SCM_USERNAME = props.getProperty("acm2.scm.username");
		ACM2_SCM_PASSWORD = props.getProperty("acm2.scm.password");
		ACM2_CSB_USERNAME = props.getProperty("acm2.csb.username");
		ACM2_CSB_PASSWORD = props.getProperty("acm2.csb.password");
		ACM2_MANIFEST = props.getProperty("acm2.manifest");
	}

	private ACM2Impl() {
		scmManager = new BasicScmManager();
		// Add all SCM providers we want to use
		scmManager.setScmProvider("git", new GitExeScmProvider());
		scmManager.setScmProvider("svn", new SvnExeScmProvider());
	}

	public static ACM2Impl getInstance() {
		if (instance == null) {
			synchronized (ACM2Impl.class) {
				if (instance == null) {
					instance = new ACM2Impl();
				}
			}
		}
		return instance;
	}
	
	public ScmClientSession getSession() {
		return ScmClient.createSession(ACM2_URL, ACM2_SCM_USERNAME, ACM2_SCM_PASSWORD);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Repository getRepository(String name, String repositoryType) {
		ScmClientSession session = getSession();
		Repository r = session.getRepositoryHandler().get(repositoryType, name);
		session.close();
		return r;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void createRepository(Repository repository) {
		ScmClientSession session = getSession();
		session.getRepositoryHandler().create(repository);
		session.close();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void createRepository(Repository repository, Manifest manifest) {
		createRepository(repository);
		addManifest(repository, manifest);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void createRepository(String name, String type) {
		Repository repository = new Repository();
		repository.setName(name);
		repository.setType(type);
		createRepository(repository);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void deleteRepository(Repository repository) {
		ScmClientSession session = getSession();
		session.getRepositoryHandler().delete(repository);
		session.close();
	}
	
	@Override
	public File zipRepository(Repository repository) {
		ScmRepository scmRepository = getScmRepository(repository);
		File workingDirectory = null;
		try {
			workingDirectory = createTmpDirectory();
			checkOut(scmRepository, workingDirectory);
			File zipFile = createTmpFile();
			ZipUtils.zipDir(workingDirectory, zipFile);
			return zipFile;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// remove temporary directory
			try {
				FileUtils.forceDelete(workingDirectory);
			} catch (IOException e) {
				logger.error("An error occurs deleting the temporary directory. Stack Trace: {}", e.getMessage());
			}
		}
		
		return null;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void deleteRepository(String name) {
		ScmClientSession session = getSession();
		session.getRepositoryHandler().delete(name);
		session.close();
	}

	// /**
	// * @inheritDoc
	// */
	// @Override
	// public File zipRepository(Repository repository) {
	// ScmRepository scmRepository = getScmRepository(repository);
	// File workingDirectory = createTmpDirectory();
	// CheckOutScmResult result = checkOut(scmRepository, workingDirectory);
	// checkResult(result);
	// List<ScmFile> checkedOutFiles = result.getCheckedOutFiles();
	//
	// logger.debug("Checked out these files: ");
	// ZipUtils.zipDir(workingDirectory, zipFile);
	// for (Iterator<ScmFile> it = checkedOutFiles.iterator(); it.hasNext();) {
	// ScmFile file = (ScmFile) it.next();
	// logger.debug(" " + file.getPath());
	// }
	//
	// // delete temporary directory
	// try {
	// FileUtils.forceDelete(workingDirectory);
	// } catch (IOException e) {
	// logger.error("An error occurs deleting the temporary directory. Stack Trace: {}",
	// e.getMessage());
	// }
	// return null;
	// }

	private CheckOutScmResult checkOut(ScmRepository repository, File workingDirectory) {
		try {
			return getScmManager().checkOut(repository, new ScmFileSet(workingDirectory));
		} catch (ScmException e) {
			logger.error("An error occurred checking out repository. Strack trace: {}", e.getMessage());
		}
		return null;
	}

	private File createTmpDirectory() throws IOException {
		File dir = null;
		try {
			dir = File.createTempFile("csb-" + RandomStringUtils.randomAlphanumeric(10), null);
			dir.delete();
			dir.mkdir();
		} catch (IOException e) {
			logger.error("Could not create temporary directory. Strack trace: {}", e.getMessage());
			throw e;
		}
		return dir;
	}
	
	private File createTmpFile() throws IOException {
		File file = null;
		try {
			file = File.createTempFile("csb-" + RandomStringUtils.randomAlphanumeric(10), null);
		} catch (IOException e) {
			logger.error("Could not create temporary file. Strack trace: {}", e.getMessage());
			throw e;
		}
		return file;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getUrl(Repository repository) {
		return repository.createUrl(ACM2_URL);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getUrl(String name, String type) {	
		ScmClientSession session = getSession();
		
		try {
			RepositoryClientHandler repositoryHandler = session.getRepositoryHandler();
			Repository repository = repositoryHandler.get(type, name);
			return getUrl(repository);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void createGroup(Group group) {
		ScmClientSession session = getSession();
		try {
			session.getGroupHandler().create(group);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void createGroup(String name) {
		Group group = new Group();
		group.setName(name);
		createGroup(group);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void deleteGroup(Group group) {
		ScmClientSession session = getSession();
		try {
			session.getGroupHandler().delete(group);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void deleteGroup(String name) {
		ScmClientSession session = getSession();
		try {
			session.getGroupHandler().delete(name);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void updateGroup(Group group) {
		ScmClientSession session = getSession();
		try {
			session.getGroupHandler().modify(group);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void createUser(User user) {
		ScmClientSession session = getSession();
		try {
			session.getUserHandler().create(user);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void createUser(String name, String displayName, String email, String password) {
		User user = new User();
		user.setName(name);
		user.setDisplayName(displayName);
		user.setPassword(password);
		createUser(user);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void deleteUser(User user) {
		ScmClientSession session = getSession();
		try {
			session.getUserHandler().delete(user);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void deleteUser(String name) {
		ScmClientSession session = getSession();
		try {
			session.getUserHandler().delete(name);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void updateUser(User user) {
		ScmClientSession session = getSession();
		try {
			session.getUserHandler().modify(user);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void addGroupMember(Group group, String user) {
		group.getMembers().add(user);
		ScmClientSession session = getSession();
		try {
			session.getGroupHandler().modify(group);
		} finally {
			session.close();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void addGroupMember(String group, String user) {
		ScmClientSession session = getSession();
		try {
			Group g = session.getGroupHandler().get(group);
			addGroupMember(g, user);
		} finally {
			session.close();
		}
	}

	/**
	 * @throws ScmException
	 * @inheritDoc
	 */
	@Override
	public void addManifest(Repository repository, Manifest manifest) {
		File baseDir = null;
		ScmRepository scmRepository = getScmRepository(repository);
		ScmResult result;

		try {
			// 1. check out repository
			baseDir = createTmpDirectory();
			result = checkOut(scmRepository, baseDir);
			checkResult(result);

			// 2. add manifest
			File file = fileManifest(manifest, baseDir);
			List<File> files = new ArrayList<File>();
			files.add(file);

			ScmFileSet fileSet = new ScmFileSet(baseDir, files);
			result = getScmManager().add(scmRepository, fileSet);
			checkResult(result);

			// 3. commit / check in
			getScmManager().checkIn(scmRepository, fileSet, "Added file " + ACM2_MANIFEST);

			// 4. push changes
			result = getScmManager().update(scmRepository, new ScmFileSet(baseDir));
			checkResult(result);
		} catch (NoSuchScmProviderException e) {
			logger.error("Repository of type {} not supported", repository.getType());
		} catch (ScmException e) {
			logger.error("An error occured in the scm repository add operation. Stack Trace: {}", e.getMessage());
		} catch (IOException e) {
			logger.error("An error occured creating the temporary directory. Stack Trace: {}", e.getMessage());
		} finally {
			// 5. remove temporary directory
			try {
				FileUtils.forceDelete(baseDir);
			} catch (IOException e) {
				logger.error("An error occurs deleting the temporary directory. Stack Trace: {}", e.getMessage());
			}
		}
	}
	
	@Override
	public Manifest getManifest(Repository repository) throws FileNotFoundException {
		ScmRepository scmRepository = getScmRepository(repository);
		File workingDirectory = null;
		try {
			workingDirectory = createTmpDirectory();
			checkOut(scmRepository, workingDirectory);
			File manifestFile = new File(workingDirectory, ACM2_MANIFEST);
			if (!manifestFile.exists()) {
				throw new FileNotFoundException("Manifest file not found");
			}
			
			// unmarshal manifest
			JAXBContext jc = JAXBContext.newInstance(Manifest.class);
			Unmarshaller u = jc.createUnmarshaller();
			return (Manifest) u.unmarshal(manifestFile);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			// remove temporary directory
			try {
				FileUtils.forceDelete(workingDirectory);
			} catch (IOException e) {
				logger.error("An error occurs deleting the temporary directory. Stack Trace: {}", e.getMessage());
			}
		}
		return null;
	}

	private ScmRepository getScmRepository(Repository repository) {
		try {
			String scmUrl = "scm:" + repository.getType() + ":" + getUrl(repository);
			ScmRepository scmRepository = getScmManager().makeScmRepository(scmUrl);
			scmRepository.getProviderRepository().setUser(ACM2_CSB_USERNAME);
			scmRepository.getProviderRepository().setPassword(ACM2_CSB_PASSWORD);
			return scmRepository;
		} catch (ScmRepositoryException e) {
			logger.error("An error occurs in the scm repository construction. Stack Trace: {}", e.getMessage());
		} catch (NoSuchScmProviderException e) {
			logger.error("Repository of type {} not supported", repository.getType());
		}
		return null;
	}

	private File fileManifest(Manifest manifest, File baseDir) {
		try {
			File file = new File(baseDir, ACM2_MANIFEST);
			JAXBContext context = JAXBContext.newInstance(Manifest.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(manifest, file);
			return file;
		} catch (JAXBException e) {
			logger.error("JAXBException while marshling manifest to file. Strack trace: {}", e.getMessage());
		}
		return null;
	}

	private ScmManager getScmManager() {
		return scmManager;
	}

	private void checkResult(ScmResult result) {
		if (!result.isSuccess()) {
			logger.error("Provider message:");
			logger.error(result.getProviderMessage() == null ? "" : result.getProviderMessage());
			logger.error("Command output:");
			logger.error(result.getCommandOutput() == null ? "" : result.getCommandOutput());
			logger.error("Command failed." + StringUtils.defaultString(result.getProviderMessage()));
		}
	}
}