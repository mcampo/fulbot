package fulbot.model.mail.outgoing;

import javax.mail.MessagingException;

import fulbot.model.Event;

/**
 * Sends replies for events.
 */
public interface EventReplier {

	public void reply(Event event) throws MessagingException;
	
}
