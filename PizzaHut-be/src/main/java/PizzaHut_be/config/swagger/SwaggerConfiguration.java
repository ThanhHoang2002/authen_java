package PizzaHut_be.config.swagger;

import PizzaHut_be.model.constant.SwaggerConstant;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "PizzaHut",
                        email = "abc@gmail.com",
                        url = "http://google.com"
                ),
                description = "OpenApi documentation for pizza-hut project",
                title = "OpenAPI specification",
                version = "1.0"
        ),
        servers = {@Server(
                description = "My Local",
                url = "http://localhost:8080/api/v1"
        )},
        security = @SecurityRequirement(
                name = SwaggerConstant.NAME
        )
)

@SecurityScheme(
        name = SwaggerConstant.NAME,
        description = "JWT token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfiguration {
}
