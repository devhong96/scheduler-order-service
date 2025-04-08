package com.scheduler.orderservice.infra.config.swagger;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Order Service Api")
)

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {


}