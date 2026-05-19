package projeto_base_de_telas_e_login.Estoque.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import projeto_base_de_telas_e_login.Estoque.Estoque;

import java.util.List;
import java.util.Optional;


public interface EstoqueRepository extends JpaRepository<Estoque, Long>, JpaSpecificationExecutor<Estoque> {

    Optional<Estoque> findByLoja_IdAndProduto_Id(Long lojaId, Long produtoId);

    List<Estoque> findByLoja_Id(Long lojaId);

}