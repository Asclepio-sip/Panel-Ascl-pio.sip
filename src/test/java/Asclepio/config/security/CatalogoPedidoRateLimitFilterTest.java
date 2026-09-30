package Asclepio.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogoPedidoRateLimitFilterTest {

    private CatalogoPedidoRateLimitFilter filtro(int porMinuto, int porHora) {
        return new CatalogoPedidoRateLimitFilter(new ObjectMapper().findAndRegisterModules(), porMinuto, porHora, false);
    }

    @Test
    void bloqueiaDepoisDoLimitePorMinutoELiberaNoMinutoSeguinte() {
        var filtro = filtro(3, 100);
        long t = 1_000_000L;

        assertTrue(filtro.registrarTentativa("1.1.1.1", t));
        assertTrue(filtro.registrarTentativa("1.1.1.1", t + 1));
        assertTrue(filtro.registrarTentativa("1.1.1.1", t + 2));
        assertFalse(filtro.registrarTentativa("1.1.1.1", t + 3));

        // outro IP não é afetado
        assertTrue(filtro.registrarTentativa("2.2.2.2", t + 3));

        // um minuto depois volta a liberar
        assertTrue(filtro.registrarTentativa("1.1.1.1", t + 60_000));
    }

    @Test
    void bloqueiaDepoisDoLimitePorHora() {
        var filtro = filtro(100, 2);
        long t = 1_000_000L;

        assertTrue(filtro.registrarTentativa("1.1.1.1", t));
        assertTrue(filtro.registrarTentativa("1.1.1.1", t + 120_000));
        assertFalse(filtro.registrarTentativa("1.1.1.1", t + 240_000));
        assertTrue(filtro.registrarTentativa("1.1.1.1", t + 3_600_000));
    }

    @Test
    void respondeComStatus429SoNaRotaDePedidoDoCatalogo() throws Exception {
        var filtro = filtro(1, 100);

        var primeira = executar(filtro, "POST", "/catalogo-online/farmacia/lojas/1/pedidos");
        var segunda = executar(filtro, "POST", "/catalogo-online/farmacia/lojas/1/pedidos");
        var listagem = executar(filtro, "GET", "/catalogo-online/farmacia/lojas/1/produtos");

        assertEquals(200, primeira.getStatus());
        assertEquals(429, segunda.getStatus());
        assertTrue(segunda.getContentAsString().contains("Muitos pedidos"));
        assertEquals(200, listagem.getStatus());
    }

    private MockHttpServletResponse executar(CatalogoPedidoRateLimitFilter filtro, String metodo, String uri) throws Exception {
        var request = new MockHttpServletRequest(metodo, uri);
        request.setRemoteAddr("9.9.9.9");
        var response = new MockHttpServletResponse();
        filtro.doFilter(request, response, new MockFilterChain());
        return response;
    }
}
