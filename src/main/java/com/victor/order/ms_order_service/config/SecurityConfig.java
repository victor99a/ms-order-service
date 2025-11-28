package com.victor.order.ms_order_service.config;


import com.victor.order.ms_order_service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Ejemplo: dejar actuator o health público si lo usas
                        .requestMatchers("/actuator/**").permitAll()

                        // 🔐 Portal TRABAJADOR (pastelero)
                        .requestMatchers(HttpMethod.GET,  "/api/pedidos/activos").hasRole("TRABAJADOR")
                        .requestMatchers(HttpMethod.PUT,  "/api/pedidos/*/preparar").hasRole("TRABAJADOR")
                        .requestMatchers(HttpMethod.PUT,  "/api/pedidos/*/enviar").hasRole("TRABAJADOR")

                        // 🔐 Portal ADMIN (todo el CRUD de pedidos, facturas, envíos)
                        .requestMatchers("/api/pedidos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/facturas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/envios/**").hasRole("ADMINISTRADOR")

                        // Cualquier otra cosa autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
