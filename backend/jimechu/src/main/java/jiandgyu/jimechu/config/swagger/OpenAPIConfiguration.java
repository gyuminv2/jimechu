package jiandgyu.jimechu.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        Server server = new Server();
        server.setUrl("/");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("gyuminv2");
        myContact.setEmail("inamemin3@gmail.com");

        Info information = new Info()
                .title("지메추 API")
                .version("1.0")
                .description("This API exposes endpoints to menu recommend.")
                .contact(myContact);
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement)
                .info(information)
                .servers(List.of(server));
    }
}