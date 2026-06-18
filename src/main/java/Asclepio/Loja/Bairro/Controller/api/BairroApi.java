package Asclepio.Loja.Bairro.Controller.api;

import Asclepio.Loja.Bairro.dto.BairroFiltroDTO;
import Asclepio.Loja.Bairro.dto.BairroRequestDTO;
import Asclepio.Loja.Bairro.dto.BairroResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/bairro")
@Tag(name = "Bairros", description = "Gerenciamento dos bairros atendidos pelas lojas")
public interface BairroApi {

    @PostMapping
    @PreAuthorize("hasAuthority('CriarBairro')")
    @Operation(
            summary = "Criar bairro",
            description = "Cria um novo bairro no sistema. O nome é obrigatório e não pode ser duplicado."
    )
    ResponseEntity<BairroResponseDTO> criar(

            @RequestBody
            BairroRequestDTO dto
    );

    @GetMapping
    @PreAuthorize("hasAuthority('VerBairro')")
    @Operation(
            summary = "Listar bairros com filtros",
            description = """
                    Lista bairros de forma paginada.
                    
                    Filtros disponíveis:
                    - id: busca pelo identificador do bairro.
                    - nome: busca parcial pelo nome do bairro.
                    
                    Também aceita paginação e ordenação:
                    - page: número da página.
                    - size: quantidade por página.
                    - sort: campo para ordenação. Exemplo: nome,asc
                    """
    )
    ResponseEntity<Page<BairroResponseDTO>> listar(

            @ParameterObject
            BairroFiltroDTO filtro,

            @ParameterObject
            Pageable pageable
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarBairro')")
    @Operation(
            summary = "Atualizar bairro",
            description = "Atualiza o nome de um bairro existente pelo ID."
    )
    ResponseEntity<BairroResponseDTO> atualizar(

            @Parameter(description = "ID do bairro", example = "1")
            @PathVariable Long id,

            @RequestBody
            BairroRequestDTO dto
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirBairro')")
    @Operation(
            summary = "Excluir bairro",
            description = "Remove um bairro pelo ID. Caso o bairro não exista, retorna erro 404 pelo tratamento global."
    )
    ResponseEntity<Void> deletarBairro(

            @Parameter(description = "ID do bairro", example = "1")
            @PathVariable Long id
    );
}