package Asclepio.Loja.Loja;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaRepository;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Loja.Loja.Repository.LojaSpecification;
import Asclepio.Loja.Loja.dto.CreateLojaRequest;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import Asclepio.Usuario.User.User;
import Asclepio.config.security.UsuarioAutenticado;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LojaService {

    private final LojaRepository repository;
    private final EmpresaRepository empresaRepository;

    public LojaService(
            LojaRepository repository,
            EmpresaRepository empresaRepository
    ) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
    }

    public LojaResponse criar(CreateLojaRequest request) {

        validarRequest(request);

        Empresa empresa = getEmpresaAtual();

        validarDuplicidade(null, request, empresa.getId());

        Loja loja = new Loja(
                null,
                request.nomeLoja().trim(),
                request.cep().trim(),
                tratarTexto(request.cnpj()),
                request.telefone().trim(),
                request.TextoDescricao(),
                request.tipoAtendimento(),
                request.imagemUrl(),
                empresa
        );

        loja.configurarFreteGratis(request.valorMinimoFreteGratis());

        return LojaResponse.fromEntity(repository.save(loja));
    }

    public Page<LojaResponse> listar(LojaFiltroDTO filtro, Pageable pageable) {

        Empresa empresa = getEmpresaAtual();

        return repository
                .findAll(LojaSpecification.filtrar(filtro, empresa.getId()), pageable)
                .map(LojaResponse::fromEntity);
    }

    public LojaResponse atualizar(Long id, CreateLojaRequest request) {

        validarRequest(request);

        Empresa empresa = getEmpresaAtual();

        Loja loja = buscarPorId(id);

        validarDuplicidade(id, request, empresa.getId());

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

        return LojaResponse.fromEntity(repository.save(loja));
    }

    public void deletar(Long id) {

        Loja loja = buscarPorId(id);

        repository.delete(loja);
    }

    private Loja buscarPorId(Long id) {

        if (id == null) {
            throw new BusinessException("ID da loja é obrigatório");
        }

        Empresa empresa = getEmpresaAtual();

        return repository
                .findByIdAndEmpresa_Id(id, empresa.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loja não encontrada"));
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

    private void validarDuplicidade(Long idAtual,
                                    CreateLojaRequest request,
                                    Long empresaId) {

        repository.findByNomeLojaIgnoreCaseAndEmpresa_Id(
                        request.nomeLoja().trim(),
                        empresaId
                )
                .ifPresent(loja -> {

                    if (idAtual == null || !loja.getId().equals(idAtual)) {
                        throw new BusinessException("Já existe uma loja com esse nome");
                    }

                });

        String cnpj = tratarTexto(request.cnpj());

        if (cnpj != null) {

            repository.findByCnpjAndEmpresa_Id(cnpj, empresaId)
                    .ifPresent(loja -> {

                        if (idAtual == null || !loja.getId().equals(idAtual)) {
                            throw new BusinessException("Já existe uma loja com esse CNPJ");
                        }

                    });
        }
    }

    private Empresa getEmpresaAtual() {

        UsuarioAutenticado autenticado =
                (UsuarioAutenticado) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User usuario = autenticado.getUser();


        if (usuario.getEmpresa() == null) {
            throw new ResourceNotFoundException(
                    "Usuário não possui empresa vinculada"
            );
        }


        return usuario.getEmpresa();
    }

    private String tratarTexto(String valor) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }
}