package autocrypt.boardinfoapi.config;

import autocrypt.boardinfoapi.common.constants.SecurityConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes(SecurityConstants.SWAGGER_KEY, createSecurityScheme()));
    }

    private Info apiInfo(){
        return new Info()
                .title("Board API")
                .description("Autocrypt Assignment")
                .contact(new Contact().name("Hong Sungjin").email("sungjin0757@naver.com"));
    }

    private SecurityScheme createSecurityScheme(){
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }
}
