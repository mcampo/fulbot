package fulbot.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.context.ConfigurableWebApplicationContext;

import com.github.jknack.mwa.ApplicationConstants;
import com.github.jknack.mwa.Startup;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

public final class FulbotApp extends Startup {

	@Override
	protected void onStartup(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
		super.onStartup(servletContext, applicationContext);

		addJerseyServlet(servletContext, applicationContext);
	}

	private void addJerseyServlet(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
//		Dynamic dispatcher = servletContext.addServlet("jersey-sevlet", new SpringServlet());
//		String namespace = applicationContext.getEnvironment().getProperty(ApplicationConstants.APP_NAMESPACE);
//		dispatcher.setInitParameter(PackagesResourceConfig.PROPERTY_PACKAGES, namespace);
//		dispatcher.setInitParameter(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
//		dispatcher.setLoadOnStartup(1);
//		dispatcher.addMapping("/api/*");
	}
	
	
}