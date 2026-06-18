package Asclepio.Categoria;

import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.exception.ApiExternaException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CategoriaStorageClient {

    private final RestTemplate restTemplate;

    @Value("${storage-service.url}")
    private String storageServiceUrl;

    public CategoriaStorageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CategoriaPageResponse listar(
            CategoriaFiltro filtro,
            Pageable pageable
    ) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(storageServiceUrl + "/categorias")
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize());

        if (filtro != null) {

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                builder.queryParam("nome", filtro.nome());
            }

            if (filtro.categoriaPaiId() != null) {
                builder.queryParam("categoriaPaiId", filtro.categoriaPaiId());
            }

            if (filtro.somentePrincipais() != null) {
                builder.queryParam("somentePrincipais", filtro.somentePrincipais());
            }

            if (filtro.ativa() != null) {
                builder.queryParam("ativa", filtro.ativa());
            }
        }

        try {

            ResponseEntity<CategoriaPageResponse> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            null,
                            CategoriaPageResponse.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {

            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public CategoriaResponse criar(CriarCategoria dto) {

        String url = storageServiceUrl + "/categorias";

        try {

            ResponseEntity<CategoriaResponse> response =
                    restTemplate.postForEntity(
                            url,
                            dto,
                            CategoriaResponse.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {

            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public CategoriaResponse editar(Long id, CriarCategoria dto) {

        String url = storageServiceUrl + "/categorias/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CriarCategoria> request =
                new HttpEntity<>(dto, headers);

        try {

            ResponseEntity<CategoriaResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            request,
                            CategoriaResponse.class
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

        String url = storageServiceUrl + "/categorias/" + id;

        try {

            restTemplate.delete(url);

        } catch (HttpClientErrorException | HttpServerErrorException ex) {

            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    private String extrairMensagem(String responseBody) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readTree(responseBody);

            if (json.has("message")) {
                return json.get("message").asText();
            }

            return "Erro ao comunicar com a API de categorias";

        } catch (Exception e) {

            return "Erro ao comunicar com a API de categorias";
        }
    }
}