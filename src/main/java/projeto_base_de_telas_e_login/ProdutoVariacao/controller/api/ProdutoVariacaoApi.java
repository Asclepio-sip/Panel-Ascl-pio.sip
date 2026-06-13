package projeto_base_de_telas_e_login.ProdutoVariacao.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.ProdutoVariacao.dto.ProdutoVariacaoAddDTO;
import projeto_base_de_telas_e_login.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import projeto_base_de_telas_e_login.ProdutoVariacao.dto.ProdutoVariacaoUpdateDTO;

import java.util.List;

@RequestMapping("/produtos/{produtoId}/variacoes")
public interface ProdutoVariacaoApi {

    @PostMapping
    @PreAuthorize("hasAuthority('CriarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponseDTO> criar(
            @PathVariable Long produtoId,
            @RequestBody ProdutoVariacaoAddDTO dto
    );

    @GetMapping
    @PreAuthorize("hasAuthority('VerProdutoVariacao')")
    ResponseEntity<List<ProdutoVariacaoResponseDTO>> listarPorProduto(
            @PathVariable Long produtoId
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponseDTO> atualizar(
            @PathVariable Long produtoId,
            @PathVariable Long id,
            @RequestBody ProdutoVariacaoUpdateDTO dto
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirProdutoVariacao')")
    ResponseEntity<Void> deletar(
            @PathVariable Long produtoId,
            @PathVariable Long id
    );
}