package Asclepio.Loja.Loja.Controller.Api;

import Asclepio.Loja.Loja.dto.CreateLojaRequest;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lojas")
@Tag(name = "Lojas", description = "Gerenciamento de lojas/farmácias")
public interface LojaApi {

    @PostMapping
    @PreAuthorize("hasAuthority('CriarLoja')")
    @Operation(
            summary = "Criar loja",
            description = "Cria uma nova loja/farmácia no sistema. Nome, CEP, telefone e tipo de atendimento são obrigatórios."
    )
    ResponseEntity<LojaResponse> criar(

            @RequestBody
            CreateLojaRequest request
    );

    @GetMapping
    @PreAuthorize("hasAuthority('VerLoja')")
    @Operation(
            summary = "Listar lojas com filtros",
            description = """
                    Lista lojas de forma paginada.
                    
                    Filtros disponíveis:
                    - id: busca pelo ID da loja.
                    - nomeLoja: busca parcial pelo nome.
                    - cep: busca parcial pelo CEP.
                    - cnpj: busca parcial pelo CNPJ.
                    - telefone: busca parcial pelo telefone.
                    - tipoAtendimento: filtra por ENTREGA, RETIRADA ou AMBOS.
                    
                    Paginação:
                    - page: número da página.
                    - size: quantidade por página.
                    - sort: campo de ordenação. Exemplo: nomeLoja,asc
                    """
    )
    ResponseEntity<Page<LojaResponse>> listar(

            @ParameterObject
            LojaFiltroDTO filtro,

            @ParameterObject
            Pageable pageable
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarLoja')")
    @Operation(
            summary = "Atualizar loja",
            description = "Atualiza os dados principais de uma loja existente pelo ID."
    )
    ResponseEntity<LojaResponse> atualizar(

            @Parameter(description = "ID da loja", example = "1")
            @PathVariable Long id,

            @RequestBody
            CreateLojaRequest request
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirLoja')")
    @Operation(
            summary = "Excluir loja",
            description = "Remove uma loja pelo ID. Caso não exista, retorna 404 pelo tratamento global de erros."
    )
    ResponseEntity<Void> deletar(

            @Parameter(description = "ID da loja", example = "1")
            @PathVariable Long id
    );
}