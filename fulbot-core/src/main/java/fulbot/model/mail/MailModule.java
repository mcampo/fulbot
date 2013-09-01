package fulbot.model.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.subethamail.wiser.Wiser;

import com.github.jknack.mwa.Mode;

@Configuration
@Import(com.github.jknack.mwa.mail.MailModule.class)
public class MailModule {

	@Bean
	public Wiser wiser(final Environment env, final Mode mode) {
		Wiser wiser = null;

		if (mode.isDev()) {
			Integer smtpPort = env.getProperty(com.github.jknack.mwa.mail.MailModule.SMTP_PORT, Integer.class, 25000);
			wiser = new Wiser(smtpPort);
			wiser.start();
		}

		return wiser;
	}

}
