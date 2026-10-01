package Asclepio.config.security;

import Asclepio.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Limita quantos pedidos um mesmo IP pode fazer pelo catálogo público (rota sem login),
// para ninguém disparar uma enxurrada de pedidos de uma vez.
// Em memória: vale por instância da aplicação e zera ao reiniciar.
@Component
public class CatalogoPedidoRateLimitFilter extends OncePerRequestFilter {

    private static final String ROTA_PEDIDO_CATALOGO = "/catalogo-online/*/lojas/*/pedidos";
    private static final long UM_MINUTO_MS = 60_000L;
    private static final long UMA_HORA_MS = 3_600_000L;
    private static final int LIMPEZA_A_CADA = 1_000;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Map<String, Deque<Long>> pedidosPorIp = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    private final int limitePorMinuto;
    private final int limitePorHora;
    private final boolean usarXForwardedFor;

    private int requisicoesDesdeUltimaLimpeza = 0;

    public CatalogoPedidoRateLimitFilter(
            ObjectMapper objectMapper,
            @Value("${catalogo.pedido.rate-limit.por-minuto:5}") int limitePorMinuto,
            @Value("${catalogo.pedido.rate-limit.por-hora:20}") int limitePorHora,
            @Value("${catalogo.pedido.rate-limit.usar-x-forwarded-for:false}") boolean usarXForwardedFor
    ) {
        this.objectMapper = objectMapper;
        this.limitePorMinuto = limitePorMinuto;
        this.limitePorHora = limitePorHora;
        this.usarXForwardedFor = usarXForwardedFor;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"POST".equalsIgnoreCase(request.getMethod())
                || !pathMatcher.match(ROTA_PEDIDO_CATALOGO, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!registrarTentativa(ipDoCliente(request), System.currentTimeMillis())) {
            responderLimiteExcedido(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    // true = pode seguir; false = passou do limite
    boolean registrarTentativa(String ip, long agora) {

        limparIpsParados(agora);

        Deque<Long> tentativas = pedidosPorIp.computeIfAbsent(ip, k -> new ArrayDeque<>());

        synchronized (tentativas) {

            while (!tentativas.isEmpty() && agora - tentativas.peekFirst() >= UMA_HORA_MS) {
                tentativas.pollFirst();
            }

            long noUltimoMinuto = tentativas.stream()
                    .filter(momento -> agora - momento < UM_MINUTO_MS)
                    .count();

            if (noUltimoMinuto >= limitePorMinuto || tentativas.size() >= limitePorHora) {
                return false;
            }

            tentativas.addLast(agora);
            return true;
        }
    }

    // De tempos em tempos remove IPs sem tentativa na última hora, para o mapa não crescer sem fim
    private void limparIpsParados(long agora) {

        synchronized (this) {
            if (++requisicoesDesdeUltimaLimpeza < LIMPEZA_A_CADA) {
                return;
            }
            requisicoesDesdeUltimaLimpeza = 0;
        }

        pedidosPorIp.entrySet().removeIf(entrada -> {
            Deque<Long> tentativas = entrada.getValue();
            synchronized (tentativas) {
                return tentativas.isEmpty() || agora - tentativas.peekLast() >= UMA_HORA_MS;
            }
        });
    }

    private String ipDoCliente(HttpServletRequest request) {

        if (usarXForwardedFor) {
            String forwardedFor = request.getHeader("X-Forwarded-For");

            if (forwardedFor != null && !forwardedFor.isBlank()) {
                // Último IP da lista = o que o nosso proxy viu; os anteriores o cliente pode inventar
                String[] ips = forwardedFor.split(",");
                return ips[ips.length - 1].trim();
            }
        }

        return request.getRemoteAddr();
    }

    private void responderLimiteExcedido(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Requests",
                "Muitos pedidos em pouco tempo. Aguarde um pouco e tente novamente.",
                request.getRequestURI()
        );

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader("Retry-After", "60");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), error);
    }
}
