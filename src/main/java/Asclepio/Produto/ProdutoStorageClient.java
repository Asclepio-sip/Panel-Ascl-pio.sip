package Asclepio.Produto;

import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import Asclepio.exception.ApiExternaException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProdutoStorageClient {

    private final RestTemplate restTemplate;

    @Value("${storage-service.url}")
    private String storageServiceUrl;

    public ProdutoStorageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProdutoStorageResponse criarProduto(
            String nome,
            String descricao,
            String marca,
            Long categoriaId,
            MultipartFile imagem
    ) {
        String url = storageServiceUrl + "/produtos";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("nome", nome);
        body.add("descricao", descricao);
        body.add("marca", marca);
        body.add("categoriaId", categoriaId);

        if (imagem != null && !imagem.isEmpty()) {
            body.add("imagem", imagem.getResource());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<ProdutoStorageResponse> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            ProdutoStorageResponse.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public PageResponse<ProdutoStorageResponse> listarProdutos(
            String nome,
            Long categoriaId,
            String nomeCategoria,
            Pageable pageable
    ) {
        String url = UriComponentsBuilder
                .fromHttpUrl(storageServiceUrl + "/produtos")
                .queryParam("nome", nome)
                .queryParam("categoriaId", categoriaId)
                .queryParam("nomeCategoria", nomeCategoria)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .toUriString();

        try {
            ResponseEntity<PageResponse<ProdutoStorageResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<PageResponse<ProdutoStorageResponse>>() {}
                    );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public void deletarProduto(Long id) {
        String url = storageServiceUrl + "/produtos/" + id;

        try {
            restTemplate.delete(url);

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ApiExternaException(
                    ex.getStatusCode().value(),
                    extrairMensagem(ex.getResponseBodyAsString())
            );
        }
    }

    public ProdutoStorageResponse atualizarProduto(
            Long id,
            ProdutoUpdateDto dto
    ) {
        String url = storageServiceUrl + "/produtos/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProdutoUpdateDto> request =
                new HttpEntity<>(dto, headers);

        try {
            ResponseEntity<ProdutoStorageResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            request,
                            ProdutoStorageResponse.class
                    );

            return response.getBody();

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

            return "Erro ao comunicar com a API de produtos";

        } catch (Exception e) {
            return "Erro ao comunicar com a API de produtos";
        }
    }
}