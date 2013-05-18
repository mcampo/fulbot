package fulbot.web;

import org.springframework.core.env.MutablePropertySources;

import com.github.jknack.mwa.Startup;

import fulbot.persistence.PersistenceModule;

public final class Fulbot extends Startup {

	
	@Override
	protected void configure(MutablePropertySources propertySources) {
		//TODO add heroku system env variables
	}

	
	@Override
	protected Class<?>[] imports() {
		return new Class<?>[] {
				PersistenceModule.class
		};
	}
	
	
}