package Asclepio.ClienteEmpresa;

import Asclepio.ClienteEmpresa.Controller.ApiClienteEmpresa;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaCreateDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaFiltro;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaResponseDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ClienteEmpresaController implements ApiClienteEmpresa {

    private final ClienteEmpresaService service;

    public ClienteEmpresaController(ClienteEmpresaService service) {
        this.service = service;
    }

    @Override
    public ClienteEmpresaResponseDTO criar(ClienteEmpresaCreateDTO dto) {
        return service.criar(dto);
    }

    @Override
    public Page<ClienteEmpresaResponseDTO> listar(ClienteEmpresaFiltro filtro, Pageable pageable) {
        return service.listar(filtro, pageable);
    }

    @Override
    public ClienteEmpresaResponseDTO atualizar(UUID id, ClienteEmpresaUpdateDTO dto) {
        return service.atualizar(id, dto);
    }
}