package Asclepio.ProdutoVariacao;

import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class RestPageProdutoVariacao extends PageImpl<ProdutoVariacaoResponseDTO> {

    @JsonCreator
    public RestPageProdutoVariacao(
            @JsonProperty("content") List<ProdutoVariacaoResponseDTO> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") long totalElements
    ) {
        super(content, PageRequest.of(number, size), totalElements);
    }
}