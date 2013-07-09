package fulbot.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import fulbot.TestHelper;
import fulbot.model.mail.MessageHeaders;
import fulbot.model.mail.incoming.MessageProcessor;
import fulbot.persistence.IncomingEmailDao;

public class IncomingEmailControllerTest {

	private ObjectMapper objectMapper;
	private MessageProcessor messageProcessor;
	private IncomingEmailController controller;
	private IncomingEmailDao incomingEmailDao;

	@Before
	public void setUp() throws Exception {
		objectMapper = new ObjectMapper();
		messageProcessor = mock(MessageProcessor.class);
		incomingEmailDao = mock(IncomingEmailDao.class);

		controller = new IncomingEmailController(objectMapper, messageProcessor, incomingEmailDao);
	}

	@Test
	public void processInboundMessageShouldSetCreateAndProcessAMimeMessage() throws Exception {
		String eventJson = TestHelper.readFile("mandrill-event-json.txt");

		controller.processInboundMessage(eventJson);

		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(messageProcessor).process(captor.capture());
		MimeMessage message = captor.getValue();
		assertFalse(StringUtils.isEmpty(message.getContent().toString()));
	}

	@Test
	public void processInboundMessageShouldSetDeliveredToHeaderToMimeMessage() throws Exception {
		String eventJson = TestHelper.readFile("mandrill-event-json.txt");
		String deliveredTo = "test@fulbot.com.ar";

		controller.processInboundMessage(eventJson);

		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(messageProcessor).process(captor.capture());
		MimeMessage message = captor.getValue();
		assertEquals(deliveredTo, message.getHeader(MessageHeaders.DELIVERED_TO, null));
	}

	@Test
	public void processInboundMessageShouldSaveReceivedJson() throws Exception {
		String eventJson = TestHelper.readFile("mandrill-event-json.txt");

		controller.processInboundMessage(eventJson);
		
		verify(incomingEmailDao).save(eq(eventJson));
	}
}
