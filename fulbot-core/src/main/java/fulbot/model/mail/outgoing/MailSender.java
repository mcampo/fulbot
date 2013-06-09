package fulbot.model.mail.outgoing;

import javax.mail.Message;
import javax.mail.MessagingException;

public interface MailSender {

	/**
	 * Sends an email message
	 * @param message
	 * @throws MessagingException
	 */
	public void send(Message message) throws MessagingException;
	
}
