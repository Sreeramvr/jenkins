package com.strand.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
 
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration	
@EnableSwagger2
public class AppConfig {
 	
 	@Bean
 	public ResponseDataGeneric rdc() {
 		return new ResponseDataGeneric();
 	}
 	
 	@Bean
 	public AppFunc appFunc() {
 		return new AppFunc();
 	}
 	
    @Bean
    public Docket swaggerAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .build()
                .apiInfo(metaData());

    }
    
    
    
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Don't do this in production, use a proper list  of allowed origins
        //config.setAllowedOrigins(Collections.singletonList("*")); 
  
        List<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("*");
        config.setAllowedOriginPatterns(allowedOriginPatterns);
        
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept","x-auth-token"));
        config.setAllowedMethods(Arrays.asList("GET", "POST"));
        config.addExposedHeader("x-auth-token");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
   

    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo("WeikField Forecast API","Spring Boot REST API for Forecast","1.0","Terms of service",
        		new Contact("Tarka", "https://tarkasoft.com", "info@tarkasoft.com "),"Apache License Version 2.0",
        		"https://www.apache.org/licenses/LICENSE-2.0",Collections.emptyList());
        

        return apiInfo;

    }
    
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {      // maintain session using header (mostly for rest session management)
        return new HeaderHttpSessionStrategy();
    }
}
