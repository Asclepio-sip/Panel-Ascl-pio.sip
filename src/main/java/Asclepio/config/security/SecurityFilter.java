package Asclepio.config.security;

import Asclepio.Usuario.User.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import Asclepio.Usuario.User.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {
            try {
                var jwt = tokenService.decodeToken(token);
                String login = jwt.getSubject();
                String tipo = jwt.getClaim("tipo").asString();

                if ("TEMP".equals(tipo)) {

                    // token temporário só pode ser usado para escolher a loja
                    if (!"/user/escolher-loja".equals(request.getRequestURI())) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    User user = userRepository.findByUsername(login).orElse(null);

                    if (user != null) {
                        UsuarioAutenticado usuarioAutenticado =
                                new UsuarioAutenticado(user, null, null, List.of());

                        var authentication = new UsernamePasswordAuthenticationToken(
                                usuarioAutenticado, token, List.of()
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                    filterChain.doFilter(request, response);
                    return;
                }

                // token FULL (fluxo que já existia)
                Long empresaId = jwt.getClaim("empresaId").asLong();
                Long lojaId = jwt.getClaim("lojaId").asLong();
                List<String> permissions =
                        jwt.getClaim("permissions").asList(String.class);

                var authorities = permissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UserDetails user = userRepository
                        .findByUsername(login)
                        .map(u -> new UsuarioAutenticado(u, empresaId, lojaId, authorities))
                        .orElse(null);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, token, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                e.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.replace("Bearer ", "").trim();
    }

}
