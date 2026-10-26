package com.feaster.api.shared.infrastructure.security;

import com.feaster.api.auth.application.commandservices.AuthCommandService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthCommandService authCommandService) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(access -> access
                        .requestMatchers("/api/health", "/actuator/health",
                                "/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/*",
                                "/api/users", "/api/users/*", "/api/users/*/posts",
                                "/api/users/*/collections", "/api/collections/public",
                                "/api/collections/*",
                                "/api/foodies/*/public-content").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(errors -> errors.authenticationEntryPoint(
                        (request, response, exception) -> response.sendError(401, "Autenticación requerida")));
        http.addFilterBefore(new TokenFilter(authCommandService), BasicAuthenticationFilter.class);
        return http.build();
    }

    private static final class TokenFilter extends OncePerRequestFilter {
        private final AuthCommandService authCommandService;

        private TokenFilter(AuthCommandService authCommandService) {
            this.authCommandService = authCommandService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain chain) throws ServletException, IOException {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7).trim();
                if (!token.isEmpty()) {
                    authCommandService.authenticate(token).ifPresent(userKey -> {
                        var authentication = new UsernamePasswordAuthenticationToken(userKey, null, List.of());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
                }
            }
            chain.doFilter(request, response);
        }
    }
}
