package fulbot.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.jknack.mwa.morphia.MorphiaModule;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

@Configuration
@Import(MorphiaModule.class)
public class PersistenceModule {

	/**
	 * Publish the application's mongo {@link DB}
	 * 
	 * @param mongo
	 * @param mongoUri
	 * @return
	 */
	@Bean
	public DB mongoDb(final Mongo mongo, final MongoURI mongoUri) {
		DB db = mongo.getDB(mongoUri.getDatabase());
		return db;
	}

}
