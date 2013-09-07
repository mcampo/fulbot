package fulbot.scheduling;

import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fulbot.model.Event;
import fulbot.model.mail.outgoing.EventReplier;
import fulbot.persistence.EventDao;

@Component
public class SendEventRepliesTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendEventRepliesTask.class);

	private EventDao eventDao;
	private EventReplier eventReplier;

	@Inject
	public SendEventRepliesTask(EventDao eventDao, EventReplier eventReplier) {
		this.eventDao = eventDao;
		this.eventReplier = eventReplier;
	}

	@Override
	public void run() {
		LOGGER.debug("Running task " + getClass().getSimpleName());

		List<Event> eventsThatNeedReply = eventDao.findEventsWithReplyPending();
		for (Event event : eventsThatNeedReply) {
			try {
				eventReplier.reply(event);
			} catch (MessagingException e) {
				LOGGER.error(String.format("Error trying to send reply for event [%s]"), e);
			}
		}
	}

}
