package fulbot.persistence;

import java.util.List;

import com.github.jmkgreen.morphia.Key;

import fulbot.model.Event;

public interface EventDao {

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
	 * 
	 * @param event
	 */
	public Key<Event> save(Event event);
}
