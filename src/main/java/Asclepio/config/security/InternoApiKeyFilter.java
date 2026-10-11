package Asclepio.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

/**
 * Autentica as rotas /interno/** pelo header X-Api-Key (chave em INTERNAL_API_KEY).
 * Quem chama é o back-end do painel SaaS (admin), não o front nem os usuários do Asclépio.
 *
 * Não é @Component de propósito: se fosse, o Spring Boot também o registraria como filtro
 * do servlet, e o OncePerRequestFilter pularia a execução dentro do Spring Security.
 */
public class InternoApiKeyFilter extends OncePerRequestFilter {

    public static final String AUTORIDADE = "SISTEMA_SAAS";
    private static final String HEADER = "X-Api-Key";

    private final byte[] chave;

    public InternoApiKeyFilter(String chave) {
        this.chave = chave == null ? new byte[0] : chave.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/interno/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String enviada = request.getHeader(HEADER);

        // Chave não configurada = ninguém entra. Comparação em tempo constante.
        if (chave.length > 0 && enviada != null
                && MessageDigest.isEqual(chave, enviada.getBytes(StandardCharsets.UTF_8))) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    "painel-saas", null, List.of(new SimpleGrantedAuthority(AUTORIDADE))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
