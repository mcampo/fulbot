package fulbot.model.mail.outgoing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fulbot.model.Event;
import fulbot.model.mail.MessageHeaders;

public class EventReplierImplTest {

	private EventReplierImpl eventReplier;
	private MailSender mailSender;
	private ContentCreator contentCreator;
	
	private Event event;

	@Before
	public void setUp() throws Exception {
		mailSender = mock(MailSender.class);
		contentCreator = mock(ContentCreator.class);
		eventReplier = new EventReplierImpl(mailSender, contentCreator);
		
		event = new Event();
		event.getEmailData().setAddress("some-address@fulbot.com");
		event.setReplyPending(false);
		
		when(contentCreator.createContent(any(Event.class))).thenReturn("dummy content");
	}

	@Test
	public void replyShouldSetReplyPendingToFalse() throws Throwable {
		eventReplier.reply(event);

		assertFalse(event.getReplyPending());
	}

	@Test
	public void replyShouldSendAnEmailReply() throws Throwable {
		eventReplier.reply(event);
		
		verify(mailSender).send(any(MimeMessage.class));
	}

	@Test(expected = MessagingException.class)
	public void replyShouldFailWhenMailSenderFails() throws Throwable {
		doThrow(MessagingException.class).when(mailSender).send(any(Message.class));
		
		eventReplier.reply(event);
		
		verify(mailSender).send(any(MimeMessage.class));
	}

	@Test
	public void replyShouldSetEmailRecipients() throws Throwable {
		event.getEmailData().setReplyTo(new HashSet<>(Arrays.asList("recipient1@test.com", "recipient2@test.com")));
		
		eventReplier.reply(event);

		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(captor.capture());
		Address[] recipients = captor.getValue().getRecipients(RecipientType.TO);
		assertEquals(2, recipients.length);
		assertEquals("recipient1@test.com", recipients[0].toString());
		assertEquals("recipient2@test.com", recipients[1].toString());
	}

	@Test
	public void replyShouldSetEmailFrom() throws Throwable {
		eventReplier.reply(event);

		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(captor.capture());
		Address[] from = captor.getValue().getFrom();
		assertEquals(1, from.length);
		assertEquals(event.getEmailData().getAddress(), from[0].toString());
	}

	@Test
	public void replyShouldSetEmailSubject() throws Throwable {
		eventReplier.reply(event);

		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(captor.capture());
		MimeMessage reply = captor.getValue();
		assertEquals("Re: " + event.getEmailData().getSubject(), reply.getSubject());
	}

	@Test
	public void replyShouldSetEmailHeaders() throws Throwable {
		event.getEmailData().setReferences(Arrays.asList("first-message", "second-message"));
		
		eventReplier.reply(event);

		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(captor.capture());
		MimeMessage reply = captor.getValue();
		assertEquals("second-message", reply.getHeader(MessageHeaders.IN_REPLY_TO, null));
	}
	
	@Test
	public void replyShouldSetContent() throws Throwable {
		String expectedContent = "expected reply content";
		when(contentCreator.createContent(eq(event))).thenReturn(expectedContent);

		eventReplier.reply(event);

		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(captor.capture());
		MimeMessage reply = captor.getValue();
		assertEquals(expectedContent, reply.getContent());
	}
}
