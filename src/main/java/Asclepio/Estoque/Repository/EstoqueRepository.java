package Asclepio.Estoque.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import Asclepio.Estoque.Estoque;

import java.util.List;
import java.util.Optional;


public interface EstoqueRepository extends JpaRepository<Estoque, Long>, JpaSpecificationExecutor<Estoque> {

    List<Estoque> findByLoja_Id(Long lojaId);

    Optional<Estoque> findByLoja_IdAndProdutoVariacao_Id(Long lojaId, Long produtoVariacaoId);

}