package fulbot.persistence;

import com.github.jmkgreen.morphia.Key;

import fulbot.model.Event;

public interface EventDao {

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
