package Asclepio.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {

    boolean existsByNomeCategoriaAndEmpresaId(String nomeCategoria, Long empresaId);

    Optional<Categoria> findByNomeCategoriaAndEmpresaId(String nomeCategoria, Long empresaId);

    Optional<Categoria> findByIdAndEmpresaId(Long id, Long empresaId);
}