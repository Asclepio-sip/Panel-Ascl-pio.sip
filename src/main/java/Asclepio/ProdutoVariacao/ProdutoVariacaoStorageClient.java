package Asclepio.ProdutoVariacao;

import Asclepio.ProdutoVariacao.dto.*;
import Asclepio.exception.ApiExternaException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProdutoVariacaoStorageClient {

    private final RestTemplate restTemplate;

    @Value("${storage-service.url}")
    private String storageServiceUrl;

    public ProdutoVariacaoStorageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProdutoVariacaoResponseDTO criar(Long produtoId, ProdutoVariacaoAddDTO dto) {
        String url = storageServiceUrl + "/variacoes/produtos/" + produtoId;

        HttpHeaders headers = criarHeadersComToken();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProdutoVariacaoAddDTO> request =
                new HttpEntity<>(dto, headers);

        try {
            ResponseEntity<ProdutoVariacaoResponseDTO> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            request,
                            ProdutoVariacaoResponseDTO.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public ProdutoVariacaoPageResponse listar(
            ProdutoVariacaoFiltro filtro,
            Pageable pageable
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(storageServiceUrl + "/variacoes")
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize());

        if (filtro != null) {

            if (filtro.id() != null) {
                builder.queryParam("id", filtro.id());
            }

            if (filtro.produtoId() != null) {
                builder.queryParam("produtoId", filtro.produtoId());
            }

            if (filtro.nomeProduto() != null && !filtro.nomeProduto().isBlank()) {
                builder.queryParam("nomeProduto", filtro.nomeProduto());
            }

            if (filtro.nomeVariacao() != null && !filtro.nomeVariacao().isBlank()) {
                builder.queryParam("nomeVariacao", filtro.nomeVariacao());
            }

            if (filtro.codigoBarras() != null && !filtro.codigoBarras().isBlank()) {
                builder.queryParam("codigoBarras", filtro.codigoBarras());
            }

            if (filtro.ativo() != null) {
                builder.queryParam("ativo", filtro.ativo());
            }
        }

        HttpHeaders headers = criarHeadersComToken();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProdutoVariacaoPageResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            request,
                            ProdutoVariacaoPageResponse.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public ProdutoVariacaoResponseDTO atualizar(
            Long id,
            ProdutoVariacaoUpdateDTO dto
    ) {
        String url = storageServiceUrl + "/variacoes/" + id;

        HttpHeaders headers = criarHeadersComToken();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProdutoVariacaoUpdateDTO> request =
                new HttpEntity<>(dto, headers);

        try {
            ResponseEntity<ProdutoVariacaoResponseDTO> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            request,
                            ProdutoVariacaoResponseDTO.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public void deletar(Long id) {
        String url = storageServiceUrl + "/variacoes/" + id;

        HttpHeaders headers = criarHeadersComToken();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public ProdutoVariacaoResponseDTO buscarPorId(Long id) {

        ProdutoVariacaoFiltro filtro = new ProdutoVariacaoFiltro(
                id,
                null,
                null,
                null,
                null,
                true
        );

        ProdutoVariacaoPageResponse page = listar(
                filtro,
                PageRequest.of(0, 1)
        );

        if (page == null || page.content() == null || page.content().isEmpty()) {
            throw new ApiExternaException(
                    404,
                    "Variação não encontrada com id: " + id
            );
        }

        return page.content().get(0);
    }

    private HttpHeaders criarHeadersComToken() {
        HttpHeaders headers = new HttpHeaders();

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            String authorization = request.getHeader("Authorization");

            if (authorization != null && !authorization.isBlank()) {
                headers.set("Authorization", authorization);
            }
        }

        return headers;
    }

    private String extrairMensagem(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(responseBody);

            if (json.has("message")) {
                return json.get("message").asText();
            }

            return "Erro ao comunicar com a API de variações";

        } catch (Exception e) {
            return "Erro ao comunicar com a API de variações";
        }
    }
}