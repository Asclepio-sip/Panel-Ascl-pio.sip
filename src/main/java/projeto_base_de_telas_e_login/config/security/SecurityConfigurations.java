package projeto_base_de_telas_e_login.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(prePostEnabled = true) // 🔥 ISSO É O PONTO-CHAVE
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Login
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // Público
                        .requestMatchers("/productsPublico/**").permitAll()

                        // Produtos
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/products/**").hasRole("ADMIN")

                        // Estoque público
                        .requestMatchers(HttpMethod.GET, "/estoque/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/estoque/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/estoque/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/estoque/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/estoque/**").hasRole("ADMIN")

                        // LOJAS PUBLICA KK
                        .requestMatchers("/lojas/**").permitAll()

                        // Loja bairros
                        .requestMatchers(HttpMethod.GET, "/loja-bairros/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/loja-bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/loja-bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/loja-bairros/**").hasRole("ADMIN")

                        // resto autenticado
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        securityFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {

        org.springframework.web.cors.CorsConfiguration configuration =
                new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOrigins(java.util.List.of(
                "http://localhost:4200",
                "https://customer-ascl-pio-sip.vercel.app/",
                "https://administration-ascl-pio-sip.vercel.app/"

        ));

        configuration.setAllowedMethods(java.util.List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(java.util.List.of("*"));

        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
