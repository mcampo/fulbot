package fulbot.scheduling;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class TaskRegistrator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRegistrator.class);
	
	private TaskScheduler scheduler;
	private ApplicationContext applicationContext;

	@Inject
	public TaskRegistrator(final TaskScheduler scheduler, final ApplicationContext applicationContext) {
		this.scheduler = scheduler;
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void registerTasks() {
		LOGGER.debug("Registering task " + SendEventRepliesTask.class.getSimpleName());
		SendEventRepliesTask sendEventRepliesTask = applicationContext.getBean(SendEventRepliesTask.class);
		scheduler.scheduleWithFixedDelay(sendEventRepliesTask, 5 * 60 * 1000);
	}
}
