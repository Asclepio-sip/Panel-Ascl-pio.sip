package Asclepio.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {
    Optional<Categoria> findByNomeCategoria(String nomeCategoria);

    List<Categoria> findByCategoriaPaiIsNull();

    List<Categoria> findByCategoriaPaiId(Long categoriaPaiId);
}