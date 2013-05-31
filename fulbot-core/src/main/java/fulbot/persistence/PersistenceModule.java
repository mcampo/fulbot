package fulbot.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.jknack.mwa.morphia.MorphiaModule;

@Configuration
@Import(MorphiaModule.class)
public class PersistenceModule {

}
