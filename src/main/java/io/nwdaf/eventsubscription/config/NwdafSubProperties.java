package io.nwdaf.eventsubscription.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nnwdaf-eventsubscription")
public record NwdafSubProperties(OpenAPIProperties openapi, ClientProperties client, Boolean init) {
	public record OpenAPIProperties(String dev_url,String prod_url) {
			
	}
	
	public record ClientProperties(String dev_url,String prod_url) {
		
	}

}

