package projeto_base_de_telas_e_login.adapter.out.persistence.Estoque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Estoque.ProdutoMenorPrecoDTO;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<EstoqueEntity, Long> {

    Optional<EstoqueEntity> findByLoja_IdAndProduto_Id(Long lojaId, Long produtoId);

    List<EstoqueEntity> findByLoja_Id(Long lojaId);

    List<EstoqueEntity> findByLoja_NomeContainingIgnoreCase(String nome);

    List<EstoqueEntity> findByProduto_NameContainingIgnoreCase(String name);


    @Query("""
SELECT e FROM EstoqueEntity e
JOIN e.loja l
WHERE (:lojaId IS NULL OR e.loja.id = :lojaId)
AND (:nomeLoja IS NULL OR l.nome ILIKE %:nomeLoja%)
AND (:semEstoque IS NULL OR 
     (:semEstoque = true AND e.quantidade = 0) OR 
     (:semEstoque = false AND e.quantidade > 0))
""")
    List<EstoqueEntity> filtrar(
            @Param("lojaId") Long lojaId,
            @Param("nomeLoja") String nomeLoja,
            @Param("semEstoque") Boolean semEstoque
    );
}