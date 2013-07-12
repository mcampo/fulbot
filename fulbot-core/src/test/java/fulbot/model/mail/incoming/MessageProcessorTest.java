package fulbot.model.mail.incoming;

import static fulbot.model.mail.MessageTestHelper.createDefaultTestMessage;
import static fulbot.model.mail.MessageTestHelper.createTestMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fulbot.model.Event;
import fulbot.model.mail.MessageHeaders;
import fulbot.persistence.EventDao;

public class MessageProcessorTest {

	private EventDao eventDao;
	private ContentReader contentReader;
	private ContentProcessor contentProcessor;
	private MessageProcessor messageProcessor;

	@Before
	public void setup() {
		eventDao = mock(EventDao.class);
		contentReader = mock(ContentReader.class);
		contentProcessor = mock(ContentProcessor.class);
		
		messageProcessor = new MessageProcessor(eventDao, contentReader, contentProcessor);
	}

	@Test
	public void shouldFindEventUsingInReplyToHeader() throws Exception {
		MimeMessage message = createDefaultTestMessage();
		message.setHeader(MessageHeaders.IN_REPLY_TO, "first-message-id");
		Event existingEvent = new Event();
		when(eventDao.findForMessageId(eq("first-message-id"))).thenReturn(existingEvent);

		messageProcessor.process(message);

		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(existingEvent, savedEvent);
	}

	@Test
	public void shouldCreateAnEventWhenNoEventIsFoundForMessage() throws Exception {
		MimeMessage message = createDefaultTestMessage();
		message.setHeader(MessageHeaders.IN_REPLY_TO, "non-existent-message-id");
		when(eventDao.findForMessageId(anyString())).thenReturn(null);

		messageProcessor.process(message);

		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(message.getSubject(), savedEvent.getEmailData().getSubject());
		assertEquals(message.getHeader(MessageHeaders.DELIVERED_TO, null), savedEvent.getEmailData().getAddress());
		assertEquals(1, savedEvent.getEmailData().getReplyTo().size());
		assertTrue(savedEvent.getEmailData().getReplyTo().contains(message.getFrom()[0].toString()));
		assertEquals(1, savedEvent.getEmailData().getReferences().size());
		assertEquals(message.getMessageID(), savedEvent.getEmailData().getReferences().get(0));
		assertTrue(savedEvent.getAttendance().isEmpty());
	}

	@Test
	public void shouldCreateAnEventWhenMessageHasNoInReplyToHeader() throws Exception {
		MimeMessage message = createDefaultTestMessage();

		messageProcessor.process(message);

		verify(eventDao, never()).findForMessageId(anyString());
		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(message.getSubject(), savedEvent.getEmailData().getSubject());
		assertEquals(message.getHeader(MessageHeaders.DELIVERED_TO, null), savedEvent.getEmailData().getAddress());
		assertEquals(1, savedEvent.getEmailData().getReplyTo().size());
		assertTrue(savedEvent.getEmailData().getReplyTo().contains(message.getFrom()[0].toString()));
		assertEquals(1, savedEvent.getEmailData().getReferences().size());
		assertEquals(message.getMessageID(), savedEvent.getEmailData().getReferences().get(0));
		assertTrue(savedEvent.getAttendance().isEmpty());
	}

	@Test
	public void shouldSetReplyToWithFromAndRecipients() throws Exception {
		String from = "test.from@domain.com";
		String ownAddress = "some-address@fulbot.com";
		//'from' address is added to recipients, but it should be added only once
		List<String> recipients = Arrays.asList("test.recipient@domain.com", ownAddress, "test.recipient.2@domain.com", "test.from@domain.com");
		MimeMessage message = createTestMessage(from, "test subject", "test content", recipients);
		message.setHeader(MessageHeaders.DELIVERED_TO, ownAddress);
	
		messageProcessor.process(message);

		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		Set<String> replyTo = savedEvent.getEmailData().getReplyTo();
		assertEquals(3, replyTo.size());
		assertTrue(replyTo.contains(from));
		assertTrue(replyTo.contains("test.recipient@domain.com"));
		assertTrue(replyTo.contains("test.recipient.2@domain.com"));
		assertFalse(replyTo.contains(ownAddress));
	}

	@Test
	public void shouldSetReplyToUsingOnlyReplyToHeaderWhenPresent() throws Exception {
		String from = "test.from@domain.com";
		String ownAddress = "some-address@fulbot.com";
		List<String> recipients = Arrays.asList("test.recipient@domain.com", ownAddress, "test.recipient.2@domain.com");
		MimeMessage message = createTestMessage(from, "test subject", "test content", recipients);
		message.setHeader(MessageHeaders.DELIVERED_TO, ownAddress);
		message.setHeader(MessageHeaders.REPLY_TO, "reply.address@domain.com");
	
		messageProcessor.process(message);

		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		Set<String> replyTo = savedEvent.getEmailData().getReplyTo();
		assertEquals(1, replyTo.size());
		assertTrue(replyTo.contains("reply.address@domain.com"));
	}

	@Test
	public void shouldUpdateEmailReferencesWhenEventIsFoundForMessage() throws Exception {
		MimeMessage message = createDefaultTestMessage();
		message.setHeader(MessageHeaders.IN_REPLY_TO, "first-message-id");
		Event existingEvent = new Event();
		existingEvent.getEmailData().setReferences(new ArrayList<String>(Arrays.asList("first-message-id", "second-message-id")));
		when(eventDao.findForMessageId(eq("first-message-id"))).thenReturn(existingEvent);
		

		messageProcessor.process(message);

		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(3, savedEvent.getEmailData().getReferences().size());
		assertEquals(message.getMessageID(), savedEvent.getEmailData().getReferences().get(2));
	}
	
	@Test
	public void shouldReadMessageContentUsingContentReader() throws Exception {
		MimeMessage message = createDefaultTestMessage();

		messageProcessor.process(message);

		verify(contentReader).read(eq(message), any(OutputStream.class));
	}

	@Test
	public void shouldProcessMessageContentUsingContentProcessor() throws Exception {
		String sender = "test.sender@domain.com";
		MimeMessage message = createDefaultTestMessage();
		message.setFrom(new InternetAddress(sender));
		message.setHeader(MessageHeaders.IN_REPLY_TO, "first-message-id");
		Event existingEvent = new Event();
		existingEvent.setAttendance(new ArrayList<String>(Arrays.asList("anAttendee", "anotherAttendee")));
		when(eventDao.findForMessageId(eq("first-message-id"))).thenReturn(existingEvent);

		messageProcessor.process(message);

		verify(contentProcessor).process(anyString(), eq(sender), eq(existingEvent.getAttendance()));
	}

	@Test
	public void shouldCleanSubjectWhenPrefixesArePresent() throws Exception {
		MimeMessage message = createDefaultTestMessage();
		String originalSubject = message.getSubject();
		message.setSubject("Re: " + originalSubject);

		messageProcessor.process(message);

		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(originalSubject, savedEvent.getEmailData().getSubject());
	}

}
