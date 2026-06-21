package Asclepio.Empresa;

import Asclepio.Empresa.dto.EmpresaFiltroDTO;
import Asclepio.Empresa.dto.EmpresaRequest;
import Asclepio.Empresa.dto.EmpresaResponse;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    private final EmpresaRepository repository;


    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    public Page<EmpresaResponse> listar(EmpresaFiltroDTO filtro, Pageable pageable) {
        return repository
                .findAll(EmpresaSpecification.filtrar(filtro), pageable)
                .map(EmpresaResponse::fromEntity);
    }

    public Empresa buscarPorId(Long id) {
        if (id == null) {
            throw new BusinessException("ID da empresa é obrigatório");
        }

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));
    }

    public EmpresaResponse buscarResponsePorId(Long id) {
        return EmpresaResponse.fromEntity(buscarPorId(id));
    }

    public EmpresaResponse criar(EmpresaRequest request) {
        validarRequest(request);
        validarDuplicidade(null, request);

        Empresa empresa = new Empresa(
                null,
                request.nome().trim(),
                tratarTexto(request.cnpj())
        );

        empresa.setAtiva(request.ativa() != null ? request.ativa() : true);

        Empresa salva = repository.save(empresa);

        return EmpresaResponse.fromEntity(salva);
    }

    public EmpresaResponse editar(Long id, EmpresaRequest request) {
        validarRequest(request);

        Empresa empresa = buscarPorId(id);

        validarDuplicidade(id, request);

        empresa.setNome(request.nome().trim());
        empresa.setCnpj(tratarTexto(request.cnpj()));

        if (request.ativa() != null) {
            empresa.setAtiva(request.ativa());
        }

        Empresa atualizada = repository.save(empresa);

        return EmpresaResponse.fromEntity(atualizada);
    }

    public void deletar(Long id) {
        Empresa empresa = buscarPorId(id);
        repository.delete(empresa);
    }

    private void validarRequest(EmpresaRequest request) {
        if (request == null) {
            throw new BusinessException("Dados da empresa são obrigatórios");
        }

        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("Nome da empresa é obrigatório");
        }
    }

    private void validarDuplicidade(Long idAtual, EmpresaRequest request) {
        String cnpj = tratarTexto(request.cnpj());

        if (cnpj != null) {
            repository.findByCnpj(cnpj)
                    .ifPresent(empresa -> {
                        if (idAtual == null || !empresa.getId().equals(idAtual)) {
                            throw new BusinessException("Já existe uma empresa com esse CNPJ");
                        }
                    });
        }
    }

    private String tratarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}