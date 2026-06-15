package Asclepio.Loja.Loja;

import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Loja.Loja.Repository.LojaSpecification;
import Asclepio.Loja.Loja.dto.CreateLojaRequest;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LojaService {

    private final LojaRepository repository;

    public LojaService(LojaRepository repository) {
        this.repository = repository;
    }

    public LojaResponse criar(CreateLojaRequest request) {

        validarRequest(request);
        validarDuplicidade(null, request);

        Loja loja = new Loja(
                null,
                request.nomeLoja().trim(),
                request.cep().trim(),
                tratarTexto(request.cnpj()),
                request.telefone().trim(),
                request.TextoDescricao(),
                request.tipoAtendimento(),
                request.imagemUrl()
        );

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        Loja salva = repository.save(loja);

        return LojaResponse.fromEntity(salva);
    }

    public Page<LojaResponse> listar(LojaFiltroDTO filtro, Pageable pageable) {
        return repository
                .findAll(LojaSpecification.filtrar(filtro), pageable)
                .map(LojaResponse::fromEntity);
    }

    public LojaResponse atualizar(Long id, CreateLojaRequest request) {

        validarRequest(request);

        Loja loja = buscarPorId(id);

        validarDuplicidade(id, request);

        loja.atualizarDados(
                request.nomeLoja().trim(),
                request.cep().trim(),
                tratarTexto(request.cnpj()),
                request.telefone().trim(),
                request.TextoDescricao(),
                request.tipoAtendimento(),
                request.imagemUrl(),
                request.valorMinimoFreteGratis()
        );

        Loja atualizada = repository.save(loja);

        return LojaResponse.fromEntity(atualizada);
    }

    public void deletar(Long id) {
        Loja loja = buscarPorId(id);
        repository.delete(loja);
    }

    private Loja buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
    }

    private void validarRequest(CreateLojaRequest request) {

        if (request == null) {
            throw new BusinessException("Dados da loja são obrigatórios");
        }

        if (request.nomeLoja() == null || request.nomeLoja().isBlank()) {
            throw new BusinessException("Nome da loja é obrigatório");
        }

        if (request.cep() == null || request.cep().isBlank()) {
            throw new BusinessException("CEP é obrigatório");
        }

        if (request.telefone() == null || request.telefone().isBlank()) {
            throw new BusinessException("Telefone é obrigatório");
        }

        if (request.tipoAtendimento() == null) {
            throw new BusinessException("Tipo de atendimento é obrigatório");
        }

        if (request.valorMinimoFreteGratis() != null
                && request.valorMinimoFreteGratis().signum() < 0) {
            throw new BusinessException("Valor mínimo de frete grátis inválido");
        }
    }

    private void validarDuplicidade(Long idAtual, CreateLojaRequest request) {

        repository.findByNomeLojaIgnoreCase(request.nomeLoja().trim())
                .ifPresent(loja -> {
                    if (idAtual == null || !loja.getId().equals(idAtual)) {
                        throw new BusinessException("Já existe uma loja com esse nome");
                    }
                });

        String cnpj = tratarTexto(request.cnpj());

        if (cnpj != null) {
            repository.findAll().stream()
                    .filter(loja -> cnpj.equals(loja.getCnpj()))
                    .findFirst()
                    .ifPresent(loja -> {
                        if (idAtual == null || !loja.getId().equals(idAtual)) {
                            throw new BusinessException("Já existe uma loja com esse CNPJ");
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