package Asclepio.Estoque.service;

import Asclepio.Empresa.EmpresaContext;
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
    private final EmpresaContext empresaContext;

    public EstoqueQueryService(EstoqueRepository estoqueRepository, ProdutoVariacaoStorageClient produtoVariacaoClient, EmpresaContext empresaContext) {
        this.estoqueRepository = estoqueRepository;
        this.produtoVariacaoClient = produtoVariacaoClient;
        this.empresaContext = empresaContext;
    }

    public Page<ListaDeEstoqueDasLojasResponse> listarTodos(EstoqueFiltro filtro, Pageable pageable) {
        return estoqueRepository.findAll(EstoqueSpecification.filtrar(filtro, empresaContext.getEmpresaId()), pageable).map(estoque -> {
            var variacao = produtoVariacaoClient.buscarPorId(estoque.getVariacaoId());

            return ListaDeEstoqueDasLojasResponse.fromDomain(estoque, variacao);
        });
    }

    public Estoque buscarPorId(Long id, Long empresaId) {
        return estoqueRepository.findByIdAndLoja_Empresa_Id(id, empresaId).orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    public Estoque buscarPorLojaEVariacao(Long lojaId, Long variacaoId, Long empresaId) {
        return estoqueRepository.findByLoja_IdAndLoja_Empresa_IdAndVariacaoId(lojaId, empresaId, variacaoId).orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }
}