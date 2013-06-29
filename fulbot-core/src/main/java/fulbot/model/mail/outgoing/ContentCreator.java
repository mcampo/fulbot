package fulbot.model.mail.outgoing;

import fulbot.model.Event;

public interface ContentCreator {

	/**
	 * Creates the content that will be used in replies. Generated content must
	 * have at least the event attendance information.
	 * 
	 * @param event
	 * @return
	 */
	public String createContent(Event event);

}
