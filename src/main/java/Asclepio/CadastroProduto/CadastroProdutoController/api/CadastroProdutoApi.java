package Asclepio.CadastroProduto.CadastroProdutoController.api;

import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/cadastro-produto")
@Tag(
        name = "Cadastro Completo de Produto",
        description = "Cadastro de produto, variação e estoque em uma única operação."
)
public interface CadastroProdutoApi {

    @Operation(
            summary = "Cadastrar produto completo",
            description = """
                    Realiza o cadastro completo do produto em uma única requisição.

                    Fluxo executado:
                    - Cria o produto
                    - Faz upload da imagem
                    - Cria a primeira variação
                    - Cria o estoque da loja

                    Ideal para a tela de cadastro do sistema.
                    """
    )
    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('CriarProduto')")
    ResponseEntity<CadastroProdutoCompletoResponse> cadastrar(
            @ModelAttribute @Valid CadastroProdutoCompletoDTO dto
    );

}