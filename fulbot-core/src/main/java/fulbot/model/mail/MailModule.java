package fulbot.model.mail;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(com.github.jknack.mwa.mail.MailModule.class)
public class MailModule {

}
