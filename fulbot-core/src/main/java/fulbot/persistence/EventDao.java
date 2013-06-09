package fulbot.persistence;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.Key;

import fulbot.model.Event;

public interface EventDao {

	/**
	 * Gets an event with the given id
	 * @param id
	 * @return
	 */
	public Event get(ObjectId id);
	
	/**
	 * Get All {@link Event}s
	 * @return
	 */
	public List<Event> getAll();
	
	/**
	 * Finds the matching Event for an email message id.
	 * @param messageId
	 * @return
	 */
	public Event findForMessageId(String messageId);

	/**
	 * Saves an {@link Event}
	 * @param event
	 */
	public Key<Event> save(Event event);
}
