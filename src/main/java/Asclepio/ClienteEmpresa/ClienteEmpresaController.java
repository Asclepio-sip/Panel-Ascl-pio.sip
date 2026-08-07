package Asclepio.ClienteEmpresa;

import Asclepio.ClienteEmpresa.Controller.ApiClienteEmpresa;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaCreateDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaResponseDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaUpdateDTO;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public List<ClienteEmpresaResponseDTO> listar() {
        return service.listar();
    }

    @Override
    public ClienteEmpresaResponseDTO atualizar(UUID id, ClienteEmpresaUpdateDTO dto) {
        return service.atualizar(id, dto);
    }
}