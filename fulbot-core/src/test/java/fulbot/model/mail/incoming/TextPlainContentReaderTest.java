package fulbot.model.mail.incoming;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

import fulbot.TestHelper;
import fulbot.model.mail.MessageTestHelper;

public class TextPlainContentReaderTest {

	private static final String RESOURCES_BASE = "fulbot/model/mail/incoming/";

	@Test
	public void testReadTextPlainOnly() throws Exception {
		String expectedContent = TestHelper.readFile(RESOURCES_BASE + "text_plain_only.expected.txt");
		MimeMessage message = MessageTestHelper.getMessage(RESOURCES_BASE + "text_plain_only.txt");

		String content = readMessageContents(message);

		assertEquals(expectedContent, content);
	}

	@Test
	public void testReadTextPlainAndTextHtml() throws Exception {
		String expectedContent = TestHelper.readFile(RESOURCES_BASE + "text_plain_and_text_html.expected.txt");
		MimeMessage message = MessageTestHelper.getMessage(RESOURCES_BASE + "text_plain_and_text_html.txt");

		String content = readMessageContents(message);

		assertEquals(expectedContent, content);
	}

	/**
	 * Uses an instance of {@link TextPlainContentReader} to read the contents
	 * of a {@link MimeMessage}.
	 * 
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String readMessageContents(MimeMessage message) throws MessagingException, IOException {
		TextPlainContentReader contentReader = new TextPlainContentReader();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		contentReader.read(message, outputStream);
		return outputStream.toString();
	}
}
