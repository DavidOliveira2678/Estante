package br.com.estante.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // IMPORTANTE: as regras são avaliadas de cima para baixo; a primeira que casa vale.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // GETs que exigem login, mesmo estando dentro de um prefixo público abaixo
                        .requestMatchers(HttpMethod.GET, "/api/reviews/me").authenticated()

                        // Administração do catálogo
                        .requestMatchers(HttpMethod.POST, "/api/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/authors/**", "/api/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**", "/api/authors/**", "/api/genres/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Leitura pública
                        .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/authors/**", "/api/genres/**",
                                "/api/reviews/**", "/api/users/**", "/api/lists/**").permitAll()

                        // Todo o resto (escrita, /api/me, /api/library, /api/comments...) exige login
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
