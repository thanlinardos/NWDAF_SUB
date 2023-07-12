package io.nwdaf.eventsubscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import io.nwdaf.eventsubscription.api.config.NwdafSubProperties;

@EnableConfigurationProperties(NwdafSubProperties.class)
@SpringBootApplication
public class NwdafSubApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(NwdafSubApplication.class, args);
		
		
	}

}
