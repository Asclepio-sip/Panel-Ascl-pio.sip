package projeto_base_de_telas_e_login.Categoria.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_base_de_telas_e_login.Categoria.Categoria;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNomeCategoria(String nomeCategoria);

}
