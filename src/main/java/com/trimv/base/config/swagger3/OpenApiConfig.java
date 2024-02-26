package com.trimv.base.config.swagger3;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true)
public class OpenApiConfig {

    private static final String BEARER_FORMAT = "JWT";
    private static final String SCHEME = "Bearer";
    private static final String SECURITY_SCHEME_NAME = "Security Scheme";

    @Value("${api.server.url: api.server.url}")
    private String serverUrl;

    @Value("${api.server.description: api.server.description}")
    private String serverDesc;

    @Value("${api.info.title: api.info.title}")
    private String title;

    @Value("${api.info.description: api.info.description}")
    private String description;

    @Value("${api.info.version: api.info.version}")
    private String version;

    /*@Value("${api.info.term-of-service: api.info.terms-of-service}")
    private String termOfService;

    @Value("${api.info.contact.name: api.info.contact.name}")
    private String contactName;

    @Value("${api.info.contact.email: api.info.contact.email}")
    private String contactEmail;

    @Value("${api.info.contact.url: api.info.contact.url}")
    private String contactUrl;

    @Value("${api.info.license.name: api.info.license.name}")
    private String licenseName;

    @Value("${api.info.license.url: api.info.license.url}")
    private String licenseUrl;*/

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(servers())
                .schemaRequirement(SECURITY_SCHEME_NAME, getSecurityScheme())
                .security(getSecurityRequirement())
                .info(info());
    }

    private Info info() {
        return new Info()
                .title(title)
                .description(description)
                .version(version);
//                .contact(new Contact().name(contactName).email(contactEmail).url(contactUrl))
//                .license(new License().name(licenseName).url(licenseUrl));
    }

    private List<Server> servers() {
        return new ArrayList<>() {{
            add(new Server().url(serverUrl).description(serverDesc));
        }};
    }

    private List<SecurityRequirement> getSecurityRequirement() {
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList(SECURITY_SCHEME_NAME);
        return List.of(securityRequirement);
    }

    private SecurityScheme getSecurityScheme() {
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.bearerFormat(BEARER_FORMAT);
        securityScheme.type(SecurityScheme.Type.HTTP);
        securityScheme.in(SecurityScheme.In.HEADER);
        securityScheme.scheme(SCHEME);
        return securityScheme;
    }
}
