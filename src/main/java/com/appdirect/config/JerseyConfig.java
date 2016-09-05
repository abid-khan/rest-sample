package com.appdirect.config;

import com.appdirect.controller.SubscriptionController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

/**
 *Jersey Configuration class
 */
@Configuration @ApplicationPath("/subscriptions") public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		// Controllers
		register(SubscriptionController.class);

		// Exception Mapper
		packages("com.appdirect");
	}
}
