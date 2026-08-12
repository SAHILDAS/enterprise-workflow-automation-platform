package com.sahildas.workflow.workflowservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI workflowOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Workflow Automation Platform - Workflow Service API")
                        .description("Production-grade workflow definition service for the Enterprise Workflow Automation Platform.")
                        .version("1.0.0-SNAPSHOT")
                        .contact(new Contact()
                                .name("Sahil Biswaprakash Das")
                                .url("https://github.com/SAHILDAS/enterprise-workflow-automation-platform")
                                .email("your-email@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/SAHILDAS/enterprise-workflow-automation-platform"));
    }
}