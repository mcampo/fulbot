package fulbot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/inbound")
public class InboundController {

	private static final Logger logger = LoggerFactory.getLogger(InboundController.class);
	
	@RequestMapping(method = RequestMethod.HEAD)
	@ResponseStatus(HttpStatus.OK)
	public void head() {
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void processEmail(@RequestBody String json) {
		logger.info(json);
	}
	
}