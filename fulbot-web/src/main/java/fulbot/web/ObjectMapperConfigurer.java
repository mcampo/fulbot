package fulbot.web;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.jknack.mwa.ComponentConfigurer;

/**
 * Configures the {@link ObjectMapper} used by jackson.
 */
@Component
public class ObjectMapperConfigurer implements ComponentConfigurer<ObjectMapper> {

	@Override
	public void configure(ObjectMapper objectMapper) throws Exception {
		SimpleModule mongoModule = new SimpleModule("MongoModule");
		mongoModule.addSerializer(ObjectId.class, new ObjectIdSerializer());
		
		objectMapper.registerModule(mongoModule);
	}

}
