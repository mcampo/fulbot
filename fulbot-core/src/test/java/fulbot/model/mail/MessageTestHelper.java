package fulbot.model.mail;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MessageTestHelper {

	public static MimeMessage createDefaultTestMessage() throws AddressException, MessagingException {
		String from = "test.from@domain.com";
		String subject = "test subject";
		String content = "test content";
		String recipient = "test.recipient@domain.com";
		return createTestMessage(from, subject, content, Arrays.asList(recipient));
	}

	public static MimeMessage createTestMessage(String from, String subject, String content, Collection<String> recipients) throws MessagingException,
			AddressException {
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setFrom(new InternetAddress(from));
		message.setSubject(subject);
		message.setText(content);
		for (String recipient : recipients) {
			message.addRecipient(RecipientType.TO, new InternetAddress(recipient));
		}
		message.setHeader(MessageHeaders.DELIVERED_TO, recipients.iterator().next());
		message.setHeader(MessageHeaders.MESSAGE_ID, UUID.randomUUID().toString());
		return message;
	}

	public static MimeMessage readMessageBytes(byte[] messageBytes) throws MessagingException {
		return new MimeMessage(Session.getDefaultInstance(new Properties()), new ByteArrayInputStream(messageBytes));
	}

	public static boolean hasRecipient(Message message, String email) throws MessagingException {
		Address[] recipients = message.getAllRecipients();
		for (Address address : recipients) {
			if (address.toString().equals(email)) {
				return true;
			}
		}
		return false;
	}

	public static String readFile(String filename) throws IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
		StringBuffer stringBuffer = new StringBuffer();

		char[] cBuff = new char[1024];
		int read = 0;
		while ((read = streamReader.read(cBuff)) > 0) {
			stringBuffer.append(cBuff, 0, read);
		}
		streamReader.close();

		return stringBuffer.toString();
	}

	public static MimeMessage getMessage(String filename) throws FileNotFoundException, MessagingException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		Properties props = new Properties(); 
	    Session session = Session.getDefaultInstance(props, null); 
		MimeMessage message = new MimeMessage(session, inputStream);
		
		return message;
	}

}
