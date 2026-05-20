package br.com.bytebank.accounts.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public OpenAPI accountsOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("ByteBank Account Service API")
                        .description("API responsible to manage ByteBank accounts")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Thales Fernandes")
                                .email("thalesgarcezf@gmail.com")
                                .url("https://github.com/thalesF93")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto")
                        .url("https://github.com/thalesF93/bytebank"));
    }
    }

