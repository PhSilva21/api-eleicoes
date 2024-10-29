package com.bandeira.api_eleicoes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configOpenApi(){
        return new OpenAPI().info(
                new Info().description("Definição de Apis para o ms Validações")
                        .version("1.0.0")
                        .title("Validações Api sistema eleitoral")
        );
    }
}
