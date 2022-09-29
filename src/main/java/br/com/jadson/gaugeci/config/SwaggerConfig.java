package br.com.jadson.gaugeci.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Access: http://localhost:8080/swagger-ui/index.html
 *
 * To see the API documentation
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.jadson.gaugeci.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("REST API of Gauge CI")
                .description(
                        "Gauge CI is a tool to calculate some CI sub-practices used during my doctor's degree \n "+
                        "The goal is have a unique tool that we can shared information about calculate CI sub-practices")
                .version("1.2")
                .license("MIT License")
                .contact(new Contact("Jadson Santos", "https://jadsonjs.wordpress.com", "jadsonjs@gmail.com"))
                .build();
    }
}
