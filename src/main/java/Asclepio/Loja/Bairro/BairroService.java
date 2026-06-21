package Asclepio.Loja.Bairro;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Loja.Bairro.Repository.BairroRepository;
import Asclepio.Loja.Bairro.Repository.BairroSpecification;
import Asclepio.Loja.Bairro.dto.BairroFiltroDTO;
import Asclepio.Loja.Bairro.dto.BairroRequestDTO;
import Asclepio.Loja.Bairro.dto.BairroResponseDTO;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;
    private final EmpresaContext empresaContext;

    public BairroService(BairroRepository bairroRepository, EmpresaContext empresaContext) {
        this.bairroRepository = bairroRepository;
        this.empresaContext = empresaContext;
    }

    public Bairro criar(BairroRequestDTO dto) {

        validarNome(dto.nome());

        Long empresaId = empresaContext.getEmpresaId();
        Empresa empresa = empresaContext.getEmpresa();

        String nomeTratado = dto.nome().trim();

        if (bairroRepository.existsByNomeIgnoreCaseAndEmpresa_Id(nomeTratado, empresaId)) {
            throw new BusinessException("Bairro já cadastrado nessa empresa");
        }

        Bairro bairro = new Bairro(null, nomeTratado, empresa);

        return bairroRepository.save(bairro);
    }

    public Page<BairroResponseDTO> listar(BairroFiltroDTO filtro, Pageable pageable) {
        return bairroRepository
                .findAll(BairroSpecification.filtrar(filtro, empresaContext.getEmpresaId()), pageable)
                .map(BairroResponseDTO::fromEntity);
    }

    public Bairro buscarPorId(Long id) {
        return bairroRepository.findByIdAndEmpresa_Id(id, empresaContext.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));
    }

    public Bairro buscarPorNome(String nome) {
        return bairroRepository.findByNomeIgnoreCaseAndEmpresa_Id(nome, empresaContext.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));
    }

    public Bairro atualizar(Long id, BairroRequestDTO dto) {

        validarNome(dto.nome());

        Bairro bairro = buscarPorId(id);

        Long empresaId = empresaContext.getEmpresaId();
        String nomeTratado = dto.nome().trim();

        bairroRepository.findByNomeIgnoreCaseAndEmpresa_Id(nomeTratado, empresaId)
                .ifPresent(bairroExistente -> {
                    if (!bairroExistente.getId().equals(id)) {
                        throw new BusinessException("Já existe outro bairro com esse nome nessa empresa");
                    }
                });

        bairro.setNome(nomeTratado);

        return bairroRepository.save(bairro);
    }

    public void deletar(Long id) {
        Bairro bairro = buscarPorId(id);
        bairroRepository.delete(bairro);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("Nome do bairro não pode ser vazio");
        }
    }
}