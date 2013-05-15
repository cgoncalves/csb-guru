package pt.it.av.atnog.csb.scheduler.jobs;

import java.util.Properties;

import org.ow2.sirocco.cimi.domain.CimiMachine;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.it.av.atnog.csb.iaasmanager.client.IaasManagerClient;
import pt.it.av.atnog.csb.util.PortScan;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
public class MachineJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(MachineJob.class);
	
	private static final String IM_URL;
	private static final String IM_USERNAME;
	private static final String IM_PASSWORD;
	
	static {
		Properties props = new Properties();
		try {
			props.load(MachineJob.class.getResourceAsStream("/im.properties"));
		} catch (Exception ex) {
			throw new RuntimeException("ppm.properties resource is not available");
		}

		IM_URL = props.getProperty("im.url");
		IM_USERNAME = props.getProperty("im.username");
		IM_PASSWORD = props.getProperty("im.password");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running MachineJob [System.err.println]");
		logger.debug("Running MachineJob");
		
//		String driver; // OBSOLETE 
		String provider; // TODO
		String username; // TODO
		String password; // TODO
		String image; // TODO
		String profile; // TODO
		
		String machineHost = "193.136.92.151"; // FIXME
		int sshPort = 22;
		
		IaasManagerClient imclient = new IaasManagerClient(IM_URL, IM_USERNAME, IM_PASSWORD);

		try {
			CimiMachine machine = imclient.createMachine();
			String machineId = IaasManagerClient.convertHrefToId(machine.getId());
			
			while(!PortScan.isPortOpen(machineHost, sshPort)) { // FIXME
				logger.info("Waiting for instance {} to up port {} (SSH)", machineId, sshPort);
				Thread.sleep(5000); // 5 seconds
			}
			
			// wait 10 more seconds just in case the SSH server is not ready yet
			Thread.sleep(10000); // 10 seconds
			
			// daisy chain job
			JobDetail jobPaas = new JobDetailImpl()
										.getJobBuilder()
										.withIdentity("paas_job")
										.ofType(PaasBootstrapJob.class)
										.usingJobData("host", machineHost)
										.usingJobData("user", "ubuntu") // FIXME
										.usingJobData("password", "ubuntu") // FIXME
										.usingJobData("paas_id", context.getJobDetail().getJobDataMap().getString("paas_id"))
										.usingJobData("domain", context.getJobDetail().getJobDataMap().getString("domain"))
										.usingJobData("callback", context.getJobDetail().getJobDataMap().getString("callback"))
									.build();
			
			Trigger trigger = new SimpleTriggerImpl()
									.getTriggerBuilder()
										.withIdentity("paas_trigger")
										.startNow()
									.build();

			Scheduler scheduler = (Scheduler) context.getJobDetail().getJobDataMap().get("scheduler");
			scheduler.scheduleJob(jobPaas, trigger);
			logger.debug("Ended running MachineJob");
		} catch (Exception e) {
			logger.error("An error occurred while provisioning a new machine or bootstrapping a new private PaaS. Error was: {}", e.getMessage());
		}
	}
}
