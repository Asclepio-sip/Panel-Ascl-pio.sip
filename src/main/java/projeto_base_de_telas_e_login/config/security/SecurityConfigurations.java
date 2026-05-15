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
                .cors()
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // =====================
                        // PUBLICO
                        // =====================
                        .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()


                        // =====================
                        //  tela de TelaInicial
                        // =====================


                        .requestMatchers("/productsPublico/**").permitAll()

                        // =====================
                        //  tela de criarUsuario
                        // =====================

                        .requestMatchers(HttpMethod.POST,"/register/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/register/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/register/**").hasRole("ADMIN")


                        // =====================
                        //  tela de TelaProdutos
                        // =====================
                        .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/products/**").hasRole("ADMIN")

                        // =====================
                        //  tela de BairroController
                        // =====================

                        .requestMatchers(HttpMethod.POST,"/bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/bairros/**").hasRole("ADMIN")

                        // =====================
                        //  tela de TelaEstoque
                        // =====================

                        .requestMatchers(HttpMethod.POST,"/estoque/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/estoque/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/estoque/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/estoque/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/estoque/filtro").hasRole("ADMIN")



                        // =====================
                        //  tela de BairroController
                        // =====================

                        .requestMatchers(HttpMethod.POST,"/loja-bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/loja-bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/loja-bairros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/loja-bairros/**").hasRole("ADMIN")



                        // =====================
                        //  tela de lojas
                        // =====================

                        .requestMatchers(HttpMethod.POST,"/lojas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/lojas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/lojas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/lojas/**").hasRole("ADMIN")



                        // =====================
                        //  tela de TelaPedido
                        // =====================
                        .requestMatchers(HttpMethod.GET,"/pedidos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/pedidos/**").hasRole("ADMIN")



                        // =====================
                        //  tela de TelaLogin
                        // =====================
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()

                        // =====================
                        //  tela de TelaLoja
                        // =====================
                        .requestMatchers(HttpMethod.GET,"/lojas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/lojas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/lojas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/lojas/**").hasRole("ADMIN")




                        // =====================
                        //  tela de TelaCategoria
                        // =====================
                        .requestMatchers(HttpMethod.POST,"/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/categorias/**").hasRole("ADMIN")

                        // PADRÃO
                        // =====================
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
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
    public org.springframework.web.servlet.config.annotation.WebMvcConfigurer corsConfigurer() {
        return new org.springframework.web.servlet.config.annotation.WebMvcConfigurer() {
            @Override
            public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
