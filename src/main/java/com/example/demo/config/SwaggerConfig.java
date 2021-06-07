package com.example.demo.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String GLOBAL = "global";
	private static final String ACCESS_EVERYTHING = "accessEverything";
	private static final String HEADER = "header";
	private static final String JWT = "JWT";
	
    @Value("${app.title: REST API title}")
    private String title;
    
    @Value("${app.name: REST API name}")
    private String name;
    
    @Value("${app.description: REST API description}")
    private String description;

	@Value("${app.version: REST API version}")
    private String version;

    @Value("${app.terms: REST API Terms of Service}")
    private String termsOfService;
    
    @Value("${app.contact.name: REST API contact name}")
    private String contactName;
    
    @Value("${app.contact.url: REST API contact url}")
    private String contactUrl;
    
    @Value("${app.contact.email: REST API contact email}")
    private String contactEmail;
    
    @Value("${app.license.name: REST API license}")
    private String licenseName;
    
    @Value("${app.license.url: REST API license url}")
    private String licenseUrl;


    private ApiInfo apiInfo() {
      return new ApiInfo(
    		  title,
    		  description,
    		  version,
    		  termsOfService,
    		  new Contact(contactName, contactUrl, contactEmail),
    		  licenseName,
    		  licenseUrl,
    		  Collections.emptyList());
    }

    @Bean
    public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)
          .apiInfo(apiInfo())
          .securityContexts(Arrays.asList(securityContext()))
          .securitySchemes(Arrays.asList(apiKey()))
          .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build();
    }

    private ApiKey apiKey() {
      return new ApiKey(JWT, AUTHORIZATION_HEADER, HEADER);
    }

    private SecurityContext securityContext() {
      return SecurityContext.builder()
          .securityReferences(defaultAuth())
          .build();
    }

    List<SecurityReference> defaultAuth() {
      AuthorizationScope authorizationScope
          = new AuthorizationScope(GLOBAL, ACCESS_EVERYTHING);
      AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
      authorizationScopes[0] = authorizationScope;
      return Arrays.asList(new SecurityReference(JWT, authorizationScopes));
    }
}
