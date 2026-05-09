package projeto_base_de_telas_e_login.adapter.out.persistence.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    Optional<CategoriaEntity> findByNomeCategoria(String nomeCategoria);

}
