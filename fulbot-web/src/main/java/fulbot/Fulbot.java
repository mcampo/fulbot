package fulbot;

import com.github.jknack.mwa.Startup;

import fulbot.persistence.PersistenceModule;

/**
 * Fulbot's {@link Startup} class
 */
public final class Fulbot extends Startup {

	@Override
	protected Class<?>[] imports() {
		return new Class<?>[] {
				PersistenceModule.class
		};
	}

}