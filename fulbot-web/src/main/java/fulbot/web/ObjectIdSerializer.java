package fulbot.web;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializes an {@link ObjectId} using its {@link ObjectId#toString()} method.
 * 
 * Used to transfer the {@link ObjectId} as a scalar value.
 */
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

	@Override
	public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(value.toString());
	}

}
