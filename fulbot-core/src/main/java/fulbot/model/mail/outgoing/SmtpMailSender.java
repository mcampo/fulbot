package fulbot.model.mail.outgoing;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.springframework.stereotype.Component;

@Component
public class SmtpMailSender implements MailSender {

	@Override
	public void send(Message message) throws MessagingException {
		// TODO Auto-generated method stub

	}

}
