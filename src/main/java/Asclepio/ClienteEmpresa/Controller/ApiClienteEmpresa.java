package Asclepio.ClienteEmpresa.Controller;

import Asclepio.ClienteEmpresa.dto.ClienteEmpresaCreateDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaResponseDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/cliente-empresa")
public interface ApiClienteEmpresa {

    @Operation(summary = "Cadastrar cliente")
    @PostMapping
    ClienteEmpresaResponseDTO criar(
            @RequestBody @Valid ClienteEmpresaCreateDTO dto
    );

    @Operation(summary = "Listar clientes da empresa")
    @GetMapping
    List<ClienteEmpresaResponseDTO> listar();

    @Operation(summary = "Atualizar cliente")
    @PutMapping("/{id}")
    ClienteEmpresaResponseDTO atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid ClienteEmpresaUpdateDTO dto
    );
}