package Asclepio.ClienteEmpresa.Controller;

import Asclepio.ClienteEmpresa.dto.ClienteEmpresaCreateDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaFiltro;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaResponseDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/cliente-empresa")
public interface ApiClienteEmpresa {

    @Operation(summary = "Cadastrar cliente")
    @PostMapping
    ClienteEmpresaResponseDTO criar(
            @RequestBody @Valid ClienteEmpresaCreateDTO dto
    );

    @Operation(summary = "Listar clientes da empresa", description = """
            Lista os clientes da empresa com paginação e filtros opcionais.
            
            Filtros disponíveis:
            
            • nome
            • numero
            • email
            """)
    @GetMapping
    Page<ClienteEmpresaResponseDTO> listar(
            @ParameterObject ClienteEmpresaFiltro filtro,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "Atualizar cliente")
    @PutMapping("/{id}")
    ClienteEmpresaResponseDTO atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid ClienteEmpresaUpdateDTO dto
    );
}