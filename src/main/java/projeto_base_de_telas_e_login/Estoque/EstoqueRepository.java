package projeto_base_de_telas_e_login.Estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto_base_de_telas_e_login.Estoque.Estoque;

import java.util.List;
import java.util.Optional;


public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByLoja_IdAndProduto_Id(Long lojaId, Long produtoId);

    List<Estoque> findByLoja_Id(Long lojaId);

    List<Estoque> findByLoja_NomeContainingIgnoreCase(String nome);

    List<Estoque> findByProduto_NameContainingIgnoreCase(String nome);

    @Query("""
            SELECT e FROM Estoque e
            JOIN e.loja l
            WHERE (:lojaId IS NULL OR e.loja.id = :lojaId)
            AND (:nomeLoja IS NULL OR LOWER(l.nome) LIKE LOWER(CONCAT('%', :nomeLoja, '%')))
            AND (:semEstoque IS NULL OR
                 (:semEstoque = true AND e.quantidade = 0) OR
                 (:semEstoque = false AND e.quantidade > 0))
            """)
    List<Estoque> filtrar(@Param("lojaId") Long lojaId, @Param("nomeLoja") String nomeLoja, @Param("semEstoque") Boolean semEstoque);
}