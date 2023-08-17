package io.nwdaf.eventsubscription.api.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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
	
	@Value("${nnwdaf-eventsubscription.openapi.dev-url}")
	private String devUrl;
	
	@Value("${nnwdaf-eventsubscription.openapi.prod-url}")
	private String prodUrl;
	
	@Value("${trust.store}")
    private Resource trustStore;
    @Value("${trust.store.password}")
    private String trustStorePassword;

    @Bean
    public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
      CertificateException, MalformedURLException, IOException {
      
        SSLContext sslContext = new SSLContextBuilder()
          .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
        SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
        .register("https", sslConFactory)
        .register("http", new PlainConnectionSocketFactory())
        .build();
        BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

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
