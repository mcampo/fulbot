package fulbot.model.mail.incoming;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import fulbot.model.Event;
import fulbot.model.mail.MessageHeaders;
import fulbot.persistence.EventDao;

/**
 * Processes an incoming message.
 * 
 * Finds or creates an event for the message and updates the attendace.
 */
public class MessageProcessor {

	private final EventDao eventDao;
	private final ContentReader contentReader;
	private final ContentProcessor contentProcessor;

	/**
	 * @param eventDao used for finding and saving events
	 */
	public MessageProcessor(EventDao eventDao, ContentReader contentReader, ContentProcessor contentProcessor) {
		this.eventDao = eventDao;
		this.contentReader = contentReader;
		this.contentProcessor = contentProcessor;
	}
	
	public void process(MimeMessage message) {
		
		try {

			String inReplyTo = message.getHeader(MessageHeaders.IN_REPLY_TO, null);
			Event event = eventDao.findForMessageId(inReplyTo);
			if (event == null) {
				event = new Event();
				event.getEmailData().setSubject(message.getSubject());
				event.getEmailData().setAddress(message.getHeader(MessageHeaders.DELIVERED_TO, null));
				event.getEmailData().getReplyTo().add(message.getFrom()[0].toString());
			}
			
			event.getEmailData().getReferences().add(message.getMessageID());
			
			String content = readContent(message);
			String sender = message.getFrom()[0].toString();
			contentProcessor.process(content, sender, event.getAttendance());
			
			eventDao.save(event);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private String readContent(MimeMessage message) throws IOException, MessagingException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		contentReader.read(message, outputStream);
		return outputStream.toString();
	}

}
