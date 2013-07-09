package fulbot.model;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
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
	public ContentProcessor contentProcessor(@Value("${commands.add}") final String[] addCommands,
												@Value("${commands.remove}") final String[] removeCommands) {
		return new CommandContentProcessor(Arrays.asList(addCommands), Arrays.asList(removeCommands));
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
