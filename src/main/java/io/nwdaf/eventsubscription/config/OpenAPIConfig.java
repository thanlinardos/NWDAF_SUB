package io.nwdaf.eventsubscription.config;

import java.util.List;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.nwdaf.eventsubscription.repository.eventnotification.entities.NnwdafNotificationTable;
import io.nwdaf.eventsubscription.repository.eventsubscription.entities.NnwdafEventsSubscriptionTable;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@RegisterReflectionForBinding({NnwdafNotificationTable.class,NnwdafEventsSubscriptionTable.class})
@EntityScan({"io.nwdaf.eventsubscription.repository.eventnotification.entities","io.nwdaf.eventsubscription.repository.eventsubscription.entities"})
public class OpenAPIConfig {
	private final NwdafSubProperties nwdafSubProperties;

    public OpenAPIConfig(NwdafSubProperties nwdafSubProperties) {
        this.nwdafSubProperties = nwdafSubProperties;
    }

    @Bean
	public OpenAPI myOpenAPI() {
		Server devServer = new Server();
		devServer.setUrl(nwdafSubProperties.openapi().dev_url());
		devServer.setDescription("Server URL in Development environment");

		Contact contact = new Contact();
		contact.setEmail("");
	    contact.setName("");
	    contact.setUrl("");

		License license = new License().name("").url("http://unlicense.org");
	    Info info = new Info()
	        .title("Nnwdaf_Eventsubscription")
		    .version("1.2.1")
		    .contact(contact)
	        .description("""
                     Nnwdaf_EventsSubscription Service API.  \r
                        © 2022, 3GPP Organizational Partners (ARIB, ATIS, CCSA, ETSI, TSDSI, TTA, TTC).  \r
                        All rights reserved.\
                    """)
	        .termsOfService("")
	        .license(license);
	    return new OpenAPI().info(info).servers(List.of(devServer));
	}
}
