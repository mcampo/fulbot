package fulbot.model.mail.incoming;

import java.util.Set;

public interface ContentProcessor {

	/**
	 * Adds or removes the sender to the attendance list based on the message
	 * content. The attendance list may remain unmodified.
	 * 
	 * @param content
	 *            the message content
	 * @param sender
	 *            the message sender
	 * @param attendance
	 *            the current attendance list
	 */
	public void process(String content, String sender, Set<String> attendance);

}
