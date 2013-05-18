package fulbot.model.mail.incoming;

import static fulbot.model.mail.MessageTestHelper.createDefaultTestMessage;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fulbot.model.Event;
import fulbot.persistence.EventDao;

public class MessageProcessorTest {

	private EventDao eventDao;
	private MessageProcessor messageProcessor;
	
	@Before
	public void setup() {
		eventDao = mock(EventDao.class);
		messageProcessor = new MessageProcessor(eventDao);
	}

	@Test
	public void shouldCreateAnEventWhenNoEventIsFoundForMessage() throws Exception {
		MimeMessage message = createDefaultTestMessage();
		when(eventDao.findForMessageId(anyString())).thenReturn(null);
		
		messageProcessor.process(message);
		
		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(savedEvent.getEmailData().getSubject(), message.getSubject());
		assertEquals(savedEvent.getEmailData().getReferences().size(), 1);
		assertEquals(savedEvent.getEmailData().getReferences().get(0), message.getMessageID());
	}
	
	@Test
	public void shouldFindEventUsingInReplyToHeader() throws Exception {
		MimeMessage message = createDefaultTestMessage();
		message.setHeader("In-Reply-To", "second-message-id");
		Event existingEvent = new Event();
		when(eventDao.findForMessageId(eq("second-message-id"))).thenReturn(existingEvent);
		
		messageProcessor.process(message);
		
		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(existingEvent, savedEvent);
	}

	@Test
	public void shouldUpdateEmailReferencesWhenEventIsFoundForMessage() throws Exception {
		MimeMessage message = createDefaultTestMessage();
		Event existingEvent = new Event();
		existingEvent.getEmailData().setReferences(new ArrayList<String>(Arrays.asList("first-message-id", "second-message-id")));
		when(eventDao.findForMessageId(anyString())).thenReturn(existingEvent);
		
		messageProcessor.process(message);
		
		ArgumentCaptor<Event> argCaptor = ArgumentCaptor.forClass(Event.class);
		verify(eventDao).save(argCaptor.capture());
		Event savedEvent = argCaptor.getValue();
		assertEquals(3, savedEvent.getEmailData().getReferences().size());
		assertEquals(message.getMessageID(), savedEvent.getEmailData().getReferences().get(2));
	}
}
