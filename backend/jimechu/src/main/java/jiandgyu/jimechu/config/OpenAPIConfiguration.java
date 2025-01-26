package jiandgyu.jimechu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
//        server.setUrl("http://localhost:8080");
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
        return new OpenAPI().info(information).servers(List.of(server));
    }
}