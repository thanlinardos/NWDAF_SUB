package io.nwdaf.eventsubscription.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Configure the behavior all properties to include non null values:
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        // Support for Instant type with dependency: com.fasterxml.jackson.datatype:jackson-datatype-jsr310
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}