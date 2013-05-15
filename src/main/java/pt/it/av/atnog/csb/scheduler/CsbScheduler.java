/**
 * 
 */
package pt.it.av.atnog.csb.scheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Startup
@Singleton
public class CsbScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(CsbScheduler.class);	
	private Scheduler scheduler;
	
	@PostConstruct
	public void init() {
		logger.debug("Starting scheduler...");
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			logger.debug("Scheduler started!");
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("Failed starting scheduler!");
		}
	}
	
	@PreDestroy
	public void destroy() {
		try {
			scheduler.shutdown();
			logger.debug("Scheduler has been shutdowned");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
}
