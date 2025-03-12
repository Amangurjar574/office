package com.example.demo2.config;

import com.example.demo2.jwt_security.JWTAuthenticationFilter;
import com.example.demo2.jwt_security.JwtAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityFilterConfig {
    @Autowired
    private JwtAuthenticationEntryPoint point;

   @Autowired
    private JWTAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
            return security.csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.disable())
                    .authorizeHttpRequests(auth -> auth.requestMatchers("/login","/v1/api/user/user","/v1/api/user/check/token","/v1/api/user/reset/password").permitAll()
                            .anyRequest().authenticated())
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                    .build();
    }
}