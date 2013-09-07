package fulbot;

import com.github.jknack.mwa.Startup;

import fulbot.model.ModelModule;
import fulbot.model.mail.MailModule;
import fulbot.persistence.PersistenceModule;
import fulbot.scheduling.SchedulingModule;

/**
 * Fulbot's {@link Startup} class
 */
public final class Fulbot extends Startup {

	@Override
	protected Class<?>[] imports() {
		return new Class<?>[] {
				ModelModule.class,
				PersistenceModule.class,
				SchedulingModule.class,
				MailModule.class
		};
	}

}