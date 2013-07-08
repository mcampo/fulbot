package fulbot.web;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import fulbot.model.mail.MessageHeaders;
import fulbot.model.mail.incoming.MessageProcessor;
import fulbot.web.to.MandrillEvent;

@Controller
@RequestMapping("/incoming")
public class IncomingEmailController {

	private static final Logger logger = LoggerFactory.getLogger(IncomingEmailController.class);

	private final ObjectMapper objectMapper;
	private final MessageProcessor messageProcessor;

	@Inject
	public IncomingEmailController(ObjectMapper objectMapper, MessageProcessor messageProcessor) {
		this.objectMapper = objectMapper;
		this.messageProcessor = messageProcessor;
	}

	@RequestMapping(method = RequestMethod.HEAD)
	@ResponseStatus(HttpStatus.OK)
	public void head() {
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void processInboundMessage(@RequestParam("mandrill_events") String json) throws Exception {
		logger.debug(json.toString());

		MandrillEvent.List mandrillEvents = objectMapper.readValue(json, MandrillEvent.List.class);
		
		for(MandrillEvent mandrillEvent : mandrillEvents) {
			String rawMessage = mandrillEvent.getMessage().getRawMessage();
			Session session = Session.getDefaultInstance(new Properties());
			MimeMessage message = new MimeMessage(session, new ByteArrayInputStream(rawMessage.getBytes("utf-8")));
			message.setHeader(MessageHeaders.DELIVERED_TO, mandrillEvent.getMessage().getEmail());
			messageProcessor.process(message);
		}
	}

}