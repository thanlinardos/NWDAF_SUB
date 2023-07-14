package io.nwdaf.eventsubscription.api.config;

import java.util.List;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.nwdaf.eventsubscription.repository.eventnotification.NnwdafNotificationTable;
import io.nwdaf.eventsubscription.repository.eventsubscription.NnwdafEventsSubscriptionTable;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@RegisterReflectionForBinding({NnwdafNotificationTable.class,NnwdafEventsSubscriptionTable.class})
public class OpenAPIConfig {
	
	@Value("${nnwdaf-eventsubscription.openapi.dev-url}")
	private String devUrl;
	
	@Value("${nnwdaf-eventsubscription.openapi.prod-url}")
	private String prodUrl;
	
	@Bean
	public OpenAPI myOpenAPI() {
		Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Server URL in Development environment");

	    Server prodServer = new Server();
	    prodServer.setUrl(prodUrl);
  	    prodServer.setDescription("Server URL in Production environment");

		Contact contact = new Contact();
		contact.setEmail("");
	    contact.setName("");
	    contact.setUrl("");

		License license = new License().name("").url("http://unlicense.org");
	    Info info = new Info()
	        .title("Nnwdaf_Eventsubscription")
		    .version("1.2.1")
		    .contact(contact)
	        .description(" Nnwdaf_EventsSubscription Service API.  \r\n"
	        		+ "    © 2022, 3GPP Organizational Partners (ARIB, ATIS, CCSA, ETSI, TSDSI, TTA, TTC).  \r\n"
	        		+ "    All rights reserved.")
	        .termsOfService("")
	        .license(license);
	    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
	}
}
