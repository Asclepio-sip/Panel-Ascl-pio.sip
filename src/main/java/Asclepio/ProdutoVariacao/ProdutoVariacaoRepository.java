package Asclepio.ProdutoVariacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoVariacaoRepository extends JpaRepository<ProdutoVariacao, Long> {

    List<ProdutoVariacao> findByProduto_Id(Long produtoId);

    boolean existsByProduto_IdAndNomeVariacaoIgnoreCase(Long produtoId, String nomeVariacao);

}