package com.github.gomestkd.startup.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String API_TITLE = "REST API's RESTful from 0 with Java, Spring Boot, Kubernetes and Docker";
    private static final String API_DESCRIPTION = "REST API's RESTful from 0 with Java, Spring Boot, Kubernetes and Docker";
    private static final String API_VERSION = "v1";
    private static final String TERMS_OF_SERVICE_URL = "https://github.com/gomes-tkd?tab=repositories";
    private static final String LICENSE_NAME = "Apache 2.0";
    private static final String LICENSE_URL = "https://github.com/gomes-tkd?tab=repositories";
    private static final String CONTACT_NAME = "José Gomes";
    private static final String CONTACT_URL = "https://github.com/gomes-tkd?tab=repositories";
    private static final String CONTACT_EMAIL = "jgomestkd@gmail.com";


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(buildInfo());
    }

    private Info buildInfo() {
        return new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .termsOfService(TERMS_OF_SERVICE_URL)
                .contact(buildContact())
                .license(buildLicense());
    }

    private Contact buildContact() {
        return new Contact()
                .name(CONTACT_NAME)
                .url(CONTACT_URL)
                .email(CONTACT_EMAIL);
    }

    private License buildLicense() {
        return new License()
                .name(LICENSE_NAME)
                .url(LICENSE_URL);
    }
}
