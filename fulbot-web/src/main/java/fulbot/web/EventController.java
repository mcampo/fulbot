package fulbot.web;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fulbot.model.Event;
import fulbot.persistence.EventDao;

@Controller
@RequestMapping("/api/events")
public class EventController {

	private final EventDao eventDao;

	@Inject
	private EventController(final EventDao eventDao) {
		Validate.notNull(eventDao,"The eventDao is required");
		this.eventDao = eventDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Event> getEvents() {
		return eventDao.getAll();
	}

}
