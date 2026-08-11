package Asclepio.ClienteEmpresa;

import Asclepio.ClienteEmpresa.dto.ClienteEmpresaCreateDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaFiltro;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaResponseDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaUpdateDTO;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Empresa.EmpresaRepository;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ClienteEmpresaService {

    private final ClienteEmpresaRepository repository;
    private final EmpresaRepository empresaRepository;
    private final EmpresaContext empresaContext;

    public ClienteEmpresaService(
            ClienteEmpresaRepository repository,
            EmpresaRepository empresaRepository,
            EmpresaContext empresaContext
    ) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
        this.empresaContext = empresaContext;
    }

    @Transactional
    public ClienteEmpresaResponseDTO criar(ClienteEmpresaCreateDTO dto) {

        Long empresaId = empresaContext.getEmpresaId();

        var empresa = empresaRepository.getReferenceById(empresaId);

        ClienteEmpresa cliente = new ClienteEmpresa();
        cliente.setNome(dto.nome());
        cliente.setNumero(dto.numero());
        cliente.setEmail(dto.email());
        cliente.setEmpresa(empresa);

        repository.save(cliente);

        return toResponse(cliente);
    }

    public Page<ClienteEmpresaResponseDTO> listar(ClienteEmpresaFiltro filtro, Pageable pageable) {

        Long empresaId = empresaContext.getEmpresaId();

        return repository
                .findAll(ClienteEmpresaSpecification.filtrar(filtro, empresaId), pageable)
                .map(this::toResponse);
    }

    @Transactional
    public ClienteEmpresaResponseDTO atualizar(UUID id, ClienteEmpresaUpdateDTO dto) {

        Long empresaId = empresaContext.getEmpresaId();

        ClienteEmpresa cliente = repository
                .findByIdAndEmpresa_Id(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        cliente.setNome(dto.nome());
        cliente.setNumero(dto.numero());
        cliente.setEmail(dto.email());

        repository.save(cliente);

        return toResponse(cliente);
    }

    private ClienteEmpresaResponseDTO toResponse(ClienteEmpresa cliente) {
        return new ClienteEmpresaResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getNumero(),
                cliente.getEmail()
        );
    }
}