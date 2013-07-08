package fulbot.model;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fulbot.model.mail.incoming.CommandContentProcessor;
import fulbot.model.mail.incoming.ContentProcessor;
import fulbot.model.mail.incoming.ContentReader;
import fulbot.model.mail.incoming.TextPlainContentReader;
import fulbot.model.mail.outgoing.ContentCreator;
import fulbot.model.mail.outgoing.MailSender;
import fulbot.model.mail.outgoing.SimpleContentCreator;
import fulbot.model.mail.outgoing.SmtpMailSender;

@Configuration
public class ModelModule {

	@Bean
	public ContentProcessor contentProcessor() {
		//TODO take commands from properties file
		return new CommandContentProcessor(new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Bean
	public ContentReader contentReader() {
		return new TextPlainContentReader();
	}

	@Bean
	public ContentCreator contentCreator() {
		return new SimpleContentCreator();
	}
	
	@Bean
	public MailSender mailSender() {
		return new SmtpMailSender();
	}
	
}
