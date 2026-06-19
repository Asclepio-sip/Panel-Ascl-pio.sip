package Asclepio.Estoque.service;

import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.Repository.EstoqueSpecification;
import Asclepio.Estoque.dto.EstoqueFiltro;
import Asclepio.Estoque.dto.ListaDeEstoqueDasLojasResponse;
import Asclepio.ProdutoVariacao.ProdutoVariacaoStorageClient;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueQueryService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoVariacaoStorageClient produtoVariacaoClient;

    public EstoqueQueryService(EstoqueRepository estoqueRepository, ProdutoVariacaoStorageClient produtoVariacaoClient) {
        this.estoqueRepository = estoqueRepository;
        this.produtoVariacaoClient = produtoVariacaoClient;
    }

    public Estoque buscarPorId(Long id) {
        return estoqueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    public Estoque buscarPorLojaEVariacao(Long lojaId, Long variacaoId) {
        return estoqueRepository.findByLoja_IdAndVariacaoId(lojaId, variacaoId).orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    public List<Estoque> buscarPorLoja(Long lojaId) {
        return estoqueRepository.findByLoja_Id(lojaId);
    }

    public Page<ListaDeEstoqueDasLojasResponse> listarTodos(EstoqueFiltro filtro, Pageable pageable) {
        return estoqueRepository.findAll(EstoqueSpecification.filtrar(filtro), pageable).map(estoque -> {
            var variacao = produtoVariacaoClient.buscarPorId(estoque.getVariacaoId());

            return ListaDeEstoqueDasLojasResponse.fromDomain(estoque, variacao);
        });
    }
}