package Asclepio.ClienteEmpresa;

import Asclepio.ClienteEmpresa.dto.ClienteEmpresaCreateDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaResponseDTO;
import Asclepio.ClienteEmpresa.dto.ClienteEmpresaUpdateDTO;
import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Empresa.EmpresaRepository;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));

        ClienteEmpresa cliente = new ClienteEmpresa();

        cliente.setNome(dto.nome());
        cliente.setNumero(dto.numero());
        cliente.setEmail(dto.email());
        cliente.setEmpresa(empresa);

        repository.save(cliente);

        return new ClienteEmpresaResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getNumero(),
                cliente.getEmail()
        );
    }

    public List<ClienteEmpresaResponseDTO> listar() {

        Long empresaId = empresaContext.getEmpresaId();

        return repository.findAllByEmpresa_IdOrderByNomeAsc(empresaId)
                .stream()
                .map(c -> new ClienteEmpresaResponseDTO(
                        c.getId(),
                        c.getNome(),
                        c.getNumero(),
                        c.getEmail()
                ))
                .toList();
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

        return new ClienteEmpresaResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getNumero(),
                cliente.getEmail()
        );
    }
}