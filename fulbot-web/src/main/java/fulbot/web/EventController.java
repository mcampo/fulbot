package fulbot.web;

import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.commons.lang3.Validate;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fulbot.model.Event;
import fulbot.model.mail.outgoing.EventReplier;
import fulbot.persistence.EventDao;

@Controller
@RequestMapping("/api/events")
public class EventController {
	
	private final EventDao eventDao;
	private final EventReplier eventReplier;

	@Inject
	public EventController(final EventDao eventDao, final EventReplier eventReplier) {
		Validate.notNull(eventDao,"The eventDao is required");
		Validate.notNull(eventReplier,"The eventReplier is required");
		this.eventDao = eventDao;
		this.eventReplier = eventReplier;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Event> getEvents() {
		return eventDao.getAll();
	}
	
	@RequestMapping(value = "/{id}/reply", method = RequestMethod.POST)
	@ResponseBody
	public void reply(@PathVariable("id") final ObjectId id) throws MessagingException {
		//TODO: handle exception
		Event event = eventDao.get(id);
		eventReplier.reply(event);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody final Event event) {
		eventDao.save(event);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable("id") final ObjectId id) {
		eventDao.deleteById(id);
	}
}
