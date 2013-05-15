package pt.it.av.atnog.csb.scheduler.jobs;

import java.util.Properties;

import javax.ws.rs.core.Response.Status;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.it.av.atnog.csb.entity.common.CSBException;
import pt.it.av.atnog.csb.entity.ppm.PrivatePaas.State;
import pt.it.av.atnog.csb.paasmanager.ppm_client.PrivatePaasManagerClient;

public class PaasBootstrapJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(PaasBootstrapJob.class);
	
	private static final String PPM_URL;
	private static final String PPM_USERNAME;
	private static final String PPM_PASSWORD;
	private static final String PPM_USER_EMAIL;
	private static final String PPM_USER_PASSWORD;
	
	static {
		Properties props = new Properties();
		try {
			props.load(PaasBootstrapJob.class.getResourceAsStream("/ppm.properties"));
		} catch (Exception ex) {
			throw new RuntimeException("ppm.properties resource is not available");
		}

		PPM_URL = props.getProperty("ppm.url");
		PPM_USERNAME = props.getProperty("ppm.username");
		PPM_PASSWORD = props.getProperty("ppm.password");
		PPM_USER_EMAIL = props.getProperty("ppm.user_email");
		PPM_USER_PASSWORD = props.getProperty("ppm.user_password");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("Running PaasBootstrapJob...");
		
		String host = context.getJobDetail().getJobDataMap().getString("host");
		String user = context.getJobDetail().getJobDataMap().getString("user");
		String password = context.getJobDetail().getJobDataMap().getString("password");
		String id = context.getJobDetail().getJobDataMap().getString("paas_id");
		String domain = context.getJobDetail().getJobDataMap().getString("domain");
		String callback = context.getJobDetail().getJobDataMap().getString("callback");
		PrivatePaasManagerClient ppmclient = new PrivatePaasManagerClient(PPM_URL, PPM_USERNAME, PPM_PASSWORD);
		
		try {
			// setup
			Status status = ppmclient.createPaas(host, user, password, id, domain, host, callback, PPM_USER_EMAIL, PPM_USER_PASSWORD);
			if (status.getFamily() != Status.Family.SUCCESSFUL) {
				throw new CSBException(Status.fromStatusCode(status.getStatusCode()), status.getReasonPhrase());
			}
			
			logger.info("Private PaaS {} bootstrapped successfully!", id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Private PaaS {} could not be bootstrapped successfully. It's state was resetted to {}!", id, State.NOT_BOOTSTRAPED);
			logger.error("Error was: {}", e.getMessage());
		}

		logger.debug("Ended running PaasBootstrapJob");
	}
}
