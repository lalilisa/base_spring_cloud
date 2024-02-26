package com.trimv.base.config.sercurities;


import com.trimv.base.config.sercurities.exception.AccessAuthenticationEntryPoint;
import com.trimv.base.config.filters.JwtRequestFilter;
import com.trimv.base.config.sercurities.exception.AuthorizationDenyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final AccessAuthenticationEntryPoint accessAuthenticationEntryPoint;
    private final AuthorizationDenyHandler authorizationDenyHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        return http
                .formLogin(lg->{
                    lg.disable();
                })
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> {
                    csrf.disable();
                })
                .cors(cors->{
                    cors.configurationSource(corsConfigurationSource());
                })
                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/error/**").permitAll();
//                    auth.requestMatchers("/swagger/**").permitAll();
//                    auth.requestMatchers("/api/v1/test").permitAll();
                    auth.anyRequest().permitAll();
//                    auth.anyRequest().permitAll();
                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.decoder(jwtDecoder())))
//                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e->{
                    e.authenticationEntryPoint(accessAuthenticationEntryPoint);
                    e.accessDeniedHandler(authorizationDenyHandler);
                })
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
