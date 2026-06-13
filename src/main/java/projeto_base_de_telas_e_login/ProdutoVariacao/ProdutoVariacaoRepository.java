package projeto_base_de_telas_e_login.ProdutoVariacao;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_base_de_telas_e_login.ProdutoVariacao.ProdutoVariacao;

import java.util.List;

public interface ProdutoVariacaoRepository
        extends JpaRepository<ProdutoVariacao, Long> {

    List<ProdutoVariacao>
    findByProduto_Id(Long produtoId);

}