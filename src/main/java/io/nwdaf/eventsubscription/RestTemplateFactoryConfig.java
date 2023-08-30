package io.nwdaf.eventsubscription;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class RestTemplateFactoryConfig {

    private Resource trustStore;
    private String trustStorePassword;

    public RestTemplateFactoryConfig(){
        this.trustStore = null;
        this.trustStorePassword = null;
    }

    public RestTemplateFactoryConfig(Resource trustStore,String trustStorePassword){
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
    }

    public ClientHttpRequestFactory createRestTemplateFactory(){
		SSLContext sslContext;
        //if there is no trust store configured use http instead
        if(this.trustStore == null || this.trustStorePassword == null){
            return new HttpComponentsClientHttpRequestFactory();
        }
		try {
			sslContext = new SSLContextBuilder()
			  .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
			SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
			.register("https", sslConFactory)
			.register("http", new PlainConnectionSocketFactory())
			.build();
			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			return requestFactory;
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
				| IOException e) {
			e.printStackTrace();
		}
        return null;
	}
}
