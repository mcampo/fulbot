package fulbot.persistence;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jmkgreen.morphia.Datastore;

/**
 * Initializes indexes defined in entities. Delegates to
 * {@link Datastore#ensureIndexes()}
 */
@Component
public class IndexesInitializer {

	private static final Logger logger = LoggerFactory.getLogger(IndexesInitializer.class);

	private Datastore datastore;

	@Inject
	public IndexesInitializer(Datastore datastore) {
		this.datastore = datastore;
	}

	@PostConstruct
	public void init() {
		logger.info("Running datastore.ensureIndexes()");
		datastore.ensureIndexes();
	}

}
