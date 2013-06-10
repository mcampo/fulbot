package fulbot.model.mail.outgoing;

import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import fulbot.model.Event;
import fulbot.model.mail.MessageHeaders;

@Component
public class EventReplierImpl implements EventReplier {

	private MailSender mailSender;

	@Inject
	public EventReplierImpl(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void reply(Event event) throws MessagingException {

		MimeMessage reply = new MimeMessage(Session.getDefaultInstance(new Properties()));

		setSubject(reply, event);
		setFrom(reply, event);
		setRecipients(reply, event);
		setInReplyToHeader(reply, event);

		mailSender.send(reply);
		event.setReplyPending(false);
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

	private void setRecipients(MimeMessage reply, Event event) throws MessagingException, AddressException {
		for (String address : event.getEmailData().getReplyTo()) {
			reply.addRecipient(RecipientType.TO, new InternetAddress(address));
		}
	}

}
