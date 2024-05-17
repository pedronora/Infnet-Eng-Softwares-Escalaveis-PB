package br.edu.infnet.socialnetwork.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("openapi")
public class OpenAPIProperties extends OpenAPI {
}
