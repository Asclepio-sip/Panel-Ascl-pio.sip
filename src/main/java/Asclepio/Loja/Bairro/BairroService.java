package Asclepio.Loja.Bairro;

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

    public BairroService(BairroRepository bairroRepository) {
        this.bairroRepository = bairroRepository;
    }

    public Bairro criar(BairroRequestDTO dto) {

        validarNome(dto.nome());

        String nomeTratado = dto.nome().trim();

        if (bairroRepository.existsByNomeIgnoreCase(nomeTratado)) {
            throw new BusinessException("Bairro já cadastrado");
        }

        Bairro bairro = new Bairro(null, nomeTratado);

        return bairroRepository.save(bairro);
    }

    public Page<BairroResponseDTO> listar(BairroFiltroDTO filtro, Pageable pageable) {
        return bairroRepository
                .findAll(BairroSpecification.filtrar(filtro), pageable)
                .map(BairroResponseDTO::fromEntity);
    }

    public Bairro buscarPorId(Long id) {
        return bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));
    }

    public Bairro buscarPorNome(String nome) {
        return bairroRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));
    }

    public Bairro atualizar(Long id, BairroRequestDTO dto) {

        validarNome(dto.nome());

        Bairro bairro = buscarPorId(id);

        String nomeTratado = dto.nome().trim();

        bairroRepository.findByNomeIgnoreCase(nomeTratado)
                .ifPresent(bairroExistente -> {
                    if (!bairroExistente.getId().equals(id)) {
                        throw new BusinessException("Já existe outro bairro com esse nome");
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