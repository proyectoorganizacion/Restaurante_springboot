package com.restaurante.restaurante_spring.security;

import com.restaurante.restaurante_spring.handler.CustomAccessDeniedHandler;
import com.restaurante.restaurante_spring.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAccessDeniedHandler customAccessDeniedHandler,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()

                        .requestMatchers("/api/v1/usuarios/registro")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers("/api/v1/usuarios/empleado")
                        .hasRole("PROPIETARIO")

                        .requestMatchers("/restaurante")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers("/api/v1/platos/registro")
                        .hasRole("PROPIETARIO")

                        .requestMatchers("/api/v1/platos/{idPlato}")
                        .hasRole("PROPIETARIO")

                        .anyRequest()
                        .authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                customAuthenticationEntryPoint
                        )
                        .accessDeniedHandler(
                                customAccessDeniedHandler
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}