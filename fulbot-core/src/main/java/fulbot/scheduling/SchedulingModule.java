package fulbot.scheduling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulingModule  {

	@Bean(destroyMethod = "shutdown")
	public TaskScheduler taskScheduler(final Environment env) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(env.getRequiredProperty("scheduler.threadPoolSize", Integer.class));
		return taskScheduler;
	}

}
