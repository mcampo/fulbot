package fulbot.model.mail.incoming;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import fulbot.model.Event;
import fulbot.persistence.EventDao;

/**
 * Processes an incoming message.
 * 
 * Finds or creates an event for the message and updates the attendace.
 */
public class MessageProcessor {

	private EventDao eventDao;

	/**
	 * @param eventDao used for finding and saving events
	 */
	public MessageProcessor(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	
	public void process(MimeMessage message) {
		
		try {

			String inReplyTo = message.getHeader("In-Reply-To", null);
			Event event = eventDao.findForMessageId(inReplyTo);
			if (event == null) {
				event = new Event();
				event.getEmailData().setSubject(message.getSubject());
			}
			
			event.getEmailData().getReferences().add(message.getMessageID());
			
			eventDao.save(event);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
