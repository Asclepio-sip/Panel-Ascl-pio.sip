package Asclepio.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;
    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;
    private final String internoApiKey;

    public SecurityConfigurations(
            SecurityFilter securityFilter,
            AuthorizationService authorizationService,
            PasswordEncoder passwordEncoder,
            @Value("${interno.api-key:}") String internoApiKey
    ) {
        this.securityFilter = securityFilter;
        this.authorizationService = authorizationService;
        this.passwordEncoder = passwordEncoder;
        this.internoApiKey = internoApiKey;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                .cors(cors -> {
                })
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/loja-bairros/lojas-com-bairros").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/planos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/estoque/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/catalogo-online/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/catalogo-online/*/lojas/*/pedidos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/bairro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pedidos/status/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pedidos/pedidos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products").permitAll()
                        // Criar conta: só o back-end do painel SaaS (header X-Api-Key, ver InternoApiKeyFilter).
                        .requestMatchers("/interno/**").hasAuthority(InternoApiKeyFilter.AUTORIDADE)



                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        securityFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new InternoApiKeyFilter(internoApiKey),
                        SecurityFilter.class
                )
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                authorizationService
        );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }


    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {

        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOrigins(
                java.util.List.of("http://localhost:4200",
                        "http://localhost:4201",
                        "https://customer-ascl-pio-sip.vercel.app",
                        "https://administration-ascl-pio-sip.vercel.app",
                        "https://administration-ascl-pio-jlb0ylz07-mateusss-projects.vercel.app/"


                ));

        configuration.setAllowedMethods(
                java.util.List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(java.util.List.of("*"));

        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}