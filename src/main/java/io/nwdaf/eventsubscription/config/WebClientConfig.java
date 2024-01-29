package io.nwdaf.eventsubscription.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.nwdaf.eventsubscription.NwdafSubApplication;
import org.springframework.core.io.Resource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class WebClientConfig {

    private static Resource trustStore;
    private static String trustStorePassword;

    public static ReactorClientHttpConnector createWebClientFactory(boolean secure) {
        try {
            KeyStore trustStoreObject = KeyStore.getInstance("PKCS12");
            trustStoreObject.load(trustStore.getInputStream(), trustStorePassword.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStoreObject);

            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(trustManagerFactory)
                    .keyStoreType("PKCS12")
                    .build();

            HttpClient httpClient = HttpClient.create().secure(sslSpec -> sslSpec.sslContext(sslContext));

            SslContext insecureSslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient insecureHttpClient = HttpClient.create()
                    .secure(sslSpec -> sslSpec.sslContext(insecureSslContext));

            if (secure) {
                return new ReactorClientHttpConnector(httpClient);
            }
            return new ReactorClientHttpConnector(insecureHttpClient);
        } catch (IOException e) {
            NwdafSubApplication.getLogger().info("Error creating WebClientFactory: " + e.getMessage());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
            NwdafSubApplication.getLogger().info("Error creating trust store: " + e.getMessage());
        }
        return null;
    }

    public static void setTrustStore(Resource trustStore) {
        WebClientConfig.trustStore = trustStore;
    }

    public static void setTrustStorePassword(String trustStorePassword) {
        WebClientConfig.trustStorePassword = trustStorePassword;
    }

    public static ExchangeStrategies createExchangeStrategies(int size_in_bytes) {
        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
        decoder.setMaxInMemorySize(size_in_bytes);
        return ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .jackson2JsonDecoder(decoder))
                .build();
    }
}
