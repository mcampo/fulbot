package fulbot.scheduling;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fulbot.model.Event;
import fulbot.model.mail.outgoing.EventReplier;
import fulbot.persistence.EventDao;

public class SendEventRepliesTaskTest {

	private EventDao eventDao;
	private EventReplier eventReplier;
	private SendEventRepliesTask task;

	@Before
	public void setUp() throws Exception {
		eventDao = mock(EventDao.class);
		eventReplier = mock(EventReplier.class);

		task = new SendEventRepliesTask(eventDao, eventReplier);
	}

	@Test
	public void runShouldNotSendAnyReplyWhenThereAreNotEventsThatNeedReply() throws Exception {
		when(eventDao.findEventsWithReplyPending()).thenReturn(new ArrayList<Event>());

		task.run();

		verify(eventReplier, never()).reply(any(Event.class));
	}

	@Test
	public void runShouldSendAReplyForEventsThatNeedReply() throws Exception {
		Event event = new Event();
		when(eventDao.findEventsWithReplyPending()).thenReturn(Arrays.asList(event));

		task.run();

		verify(eventReplier).reply(eq(event));
	}

}
