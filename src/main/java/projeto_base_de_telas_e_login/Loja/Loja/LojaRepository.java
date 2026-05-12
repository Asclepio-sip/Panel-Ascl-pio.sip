package projeto_base_de_telas_e_login.Loja.Loja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LojaRepository extends JpaRepository<Loja, Long> {

    Optional<Loja> findByNome(String nome);
}