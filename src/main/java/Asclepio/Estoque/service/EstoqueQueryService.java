package Asclepio.Estoque.service;

import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.Repository.EstoqueSpecification;
import Asclepio.Estoque.dto.EstoqueFiltro;
import Asclepio.Estoque.dto.ListaDeEstoqueDasLojasResponse;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueQueryService {

    private final EstoqueRepository estoqueRepository;

    public EstoqueQueryService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public Estoque buscarPorId(Long id) {
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    public Estoque buscarPorLojaEVariacao(Long lojaId, Long variacaoId) {
        return estoqueRepository.findByLoja_IdAndProdutoVariacao_Id(lojaId, variacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    public List<Estoque> buscarPorLoja(Long lojaId) {
        return estoqueRepository.findByLoja_Id(lojaId);
    }

    public Page<ListaDeEstoqueDasLojasResponse> listarTodos(EstoqueFiltro filtro, Pageable pageable) {
        return estoqueRepository
                .findAll(EstoqueSpecification.filtrar(filtro), pageable)
                .map(ListaDeEstoqueDasLojasResponse::fromDomain);
    }
}