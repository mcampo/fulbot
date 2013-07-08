package fulbot.model.mail.incoming;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import fulbot.model.Event;
import fulbot.model.mail.MessageHeaders;
import fulbot.persistence.EventDao;

/**
 * Processes an incoming message.
 * 
 * Finds or creates an event for the message and updates the attendace.
 */
@Component
public class MessageProcessor {

	private final EventDao eventDao;
	private final ContentReader contentReader;
	private final ContentProcessor contentProcessor;

	@Inject
	public MessageProcessor(EventDao eventDao, ContentReader contentReader, ContentProcessor contentProcessor) {
		this.eventDao = eventDao;
		this.contentReader = contentReader;
		this.contentProcessor = contentProcessor;
	}
	
	public void process(MimeMessage message) {
		
		try {

			Event event = null;
			
			// if this message is a reply to another one, try to find an event
			// that references it
			String inReplyTo = message.getHeader(MessageHeaders.IN_REPLY_TO, null);
			if (inReplyTo != null) {
				event = eventDao.findForMessageId(inReplyTo);
			}

			if (event == null) {
				event = new Event();
				event.getEmailData().setSubject(getSubject(message));
				event.getEmailData().setAddress(message.getHeader(MessageHeaders.DELIVERED_TO, null));
			}

			setReplyTo(event, message);
			
			event.getEmailData().getReferences().add(message.getMessageID());
			
			String content = readContent(message);
			String sender = message.getFrom()[0].toString();
			contentProcessor.process(content, sender, event.getAttendance());
			
			eventDao.save(event);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private String getSubject(MimeMessage message) throws MessagingException {
		String subject = message.getSubject();
		Pattern pattern = Pattern.compile("(\\[.+\\])|(Re: )", Pattern.CASE_INSENSITIVE);
		subject = pattern.matcher(subject).replaceAll("").trim();

		return subject;
	}

	private void setReplyTo(Event event, MimeMessage message) throws MessagingException {
		//reset the reply-to list
		event.getEmailData().getReplyTo().clear();

		String replyToHeader = message.getHeader(MessageHeaders.REPLY_TO, null);
		if(replyToHeader != null) {
			event.getEmailData().getReplyTo().add(replyToHeader);
			return;
		}
		
		//add email sender
		event.getEmailData().getReplyTo().add(message.getFrom()[0].toString());

		//add recipients
		for (RecipientType recipientType : Arrays.asList(RecipientType.TO, RecipientType.CC)) {
			Address[] recipients = message.getRecipients(recipientType);
			if (recipients != null) {
				for(Address recipientAddress : recipients) {
					event.getEmailData().getReplyTo().add(recipientAddress.toString());
				}
			}
		}
		
		//remove own address
		event.getEmailData().getReplyTo().remove(event.getEmailData().getAddress());
	}

	private String readContent(MimeMessage message) throws IOException, MessagingException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		contentReader.read(message, outputStream);
		return outputStream.toString();
	}

}
