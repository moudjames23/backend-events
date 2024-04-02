package com.moudjames23.eventskumojin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact =  @Contact(
                        name = "Moud",
                        email = "moud23diallo@gmail.com",
                        url = "https://github.com/moudjames23"
                ),
                description = "Documentation OpenAPI du projet de gestion d'évènements",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
