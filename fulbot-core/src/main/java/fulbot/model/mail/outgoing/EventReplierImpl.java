package fulbot.model.mail.outgoing;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import fulbot.model.Event;
import fulbot.model.mail.MessageHeaders;
import fulbot.persistence.EventDao;

@Component
public class EventReplierImpl implements EventReplier {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventReplierImpl.class);

	private final JavaMailSender mailSender;
	private final ContentCreator contentCreator;
	private final EventDao eventDao;

	@Inject
	public EventReplierImpl(JavaMailSender mailSender, ContentCreator contentCreator, EventDao eventDao) {
		this.mailSender = mailSender;
		this.contentCreator = contentCreator;
		this.eventDao = eventDao;
	}

	@Override
	public void reply(Event event) throws MessagingException {
		MimeMessage reply = mailSender.createMimeMessage();

		setSubject(reply, event);
		setFrom(reply, event);
		setRecipients(reply, event);
		setInReplyToHeader(reply, event);
		setReferencesHeader(reply, event);
		setContent(reply, event);

		LOGGER.debug("Sending reply for event " + event.getId());
		mailSender.send(reply);
		event.setReplyPending(false);
		eventDao.save(event);
	}

	private void setContent(MimeMessage reply, Event event) throws MessagingException {
		String replyContent = contentCreator.createContent(event);
		reply.setText(replyContent, "utf-8");
	}

	private void setSubject(MimeMessage reply, Event event) throws MessagingException {
		String subject = "Re: " + event.getEmailData().getSubject();
		reply.setSubject(subject);
	}

	private void setFrom(MimeMessage reply, Event event) throws AddressException, MessagingException {
		String address = event.getEmailData().getAddress();
		reply.setFrom(new InternetAddress(address));
	}

	private void setInReplyToHeader(MimeMessage reply, Event event) throws MessagingException {
		List<String> references = event.getEmailData().getReferences();
		if (!references.isEmpty()) {
			String lastReference = references.get(references.size() - 1);
			reply.setHeader(MessageHeaders.IN_REPLY_TO, lastReference);
		}
	}

	private void setReferencesHeader(MimeMessage reply, Event event) throws MessagingException {
		List<String> references = event.getEmailData().getReferences();
		if (!references.isEmpty()) {
			StringBuilder referencesBuilder = new StringBuilder();
			Iterator<String> referencesIterator = references.iterator();
			referencesBuilder.append(referencesIterator.next());
			while (referencesIterator.hasNext()) {
				referencesBuilder.append(" ");
				referencesBuilder.append(referencesIterator.next());
			}
			reply.setHeader(MessageHeaders.REFERENCES, referencesBuilder.toString());
		}
	}

	private void setRecipients(MimeMessage reply, Event event) throws MessagingException, AddressException {
		for (String address : event.getEmailData().getReplyTo()) {
			reply.addRecipient(RecipientType.TO, new InternetAddress(address));
		}
	}

}
