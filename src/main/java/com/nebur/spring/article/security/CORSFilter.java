package com.nebur.spring.article.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSFilter {

    private static final String[] ALLOWED_METHODS = {"OPTIONS", "GET", "POST", "PUT", "DELETE"};

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        for(String ALLOWED_METHOD :  ALLOWED_METHODS) {
            config.addAllowedMethod(ALLOWED_METHOD);
        }
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}