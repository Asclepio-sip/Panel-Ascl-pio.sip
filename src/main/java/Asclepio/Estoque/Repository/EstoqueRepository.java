package Asclepio.Estoque.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import Asclepio.Estoque.Estoque;

import java.util.List;
import java.util.Optional;


public interface EstoqueRepository extends JpaRepository<Estoque, Long>, JpaSpecificationExecutor<Estoque> {

    List<Estoque> findByLoja_Id(Long lojaId);

    Optional<Estoque> findByLoja_IdAndVariacaoId(Long lojaId, Long variacaoId);

    boolean existsByLoja_IdAndVariacaoId(Long lojaId, Long variacaoId);

    List<Estoque> findByLoja_IdAndLoja_Empresa_Id(Long lojaId, Long empresaId);

    Optional<Estoque> findByIdAndLoja_Empresa_Id(Long id, Long empresaId);

    Optional<Estoque> findByLoja_IdAndLoja_Empresa_IdAndVariacaoId(Long lojaId, Long empresaId, Long variacaoId);
}