package com.server.main;

import static spark.Spark.secure;

import java.io.IOException;

import javax.inject.Inject;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.server.conf.Configuration;
import com.server.conf.RedisClient;
import com.server.http.HelloWorld;
import com.server.http.QuestionResource;
import com.server.http.RedisTest;
import com.server.http.SparkFilter;
import com.server.http.UserResource;
import com.server.service.QuestionTtlService;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	@Inject
	private UserResource userResource;
	@Inject
	private RedisTest redistTest;
	@Inject
	private QuestionResource questionResource;
	@Inject
	private Configuration configuration;
	@Inject
	private SparkFilter sparkFilter;
	@Inject
	private RedisClient redisClient;
	@Inject
    private JobFactory cdiJobFactory;

	public static void main(String[] args) throws IOException, SchedulerException {
		WeldContainer container = new Weld().initialize();
		container.instance().select(Main.class).get().execute();
	}

	public void execute() throws IOException, SchedulerException {
		configuration.load();
		redisClient.load(configuration.getCacheAddress(), configuration.getCachePort());
		
		if (configuration.isSecureEnabled()) {
			secure(configuration.getKeyStorePath(), configuration.getKeyStorePass(), null, null);
			logger.info("Secure enabled");
		}

		registerResources();
		registerServices();

		logger.info("Main executed");
	}

	private void registerResources() {
		HelloWorld.registerResource();
		sparkFilter.registerResource();
		userResource.registerResource();
		redistTest.registerResource();
		questionResource.registerResource();
	}
	
	private void registerServices() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(QuestionTtlService.class).withIdentity("questionTtlServiceJob", "group1").build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("questionTtlServiceTrigger", "group1").withSchedule(SimpleScheduleBuilder.simpleSchedule()
			.withIntervalInSeconds(30).repeatForever()).build();
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.setJobFactory(cdiJobFactory);
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
	}
}
