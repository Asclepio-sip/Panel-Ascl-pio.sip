package projeto_base_de_telas_e_login.domain.repository;

import projeto_base_de_telas_e_login.adapter.in.web.dto.Estoque.ProdutoMenorPrecoDTO;
import projeto_base_de_telas_e_login.domain.model.estoque.Estoque;

import java.util.List;
import java.util.Optional;

public interface EstoquePorta {

    void save(Estoque estoque);

    Optional<Estoque> buscarPorLojaEProduto(Long lojaId, Long produtoId);

    List<Estoque> buscarPorLoja(Long lojaId);

    List<Estoque> listarTodos();

    List<Estoque> buscarPorNomeLoja(String nomeLoja);

    List<Estoque> buscarPorNomeProduto(String nomeProduto);

    void deleteById(Long id);

    List<Estoque> filtrar(Long lojaId, String nomeLoja, Boolean semEstoque);}