package projeto_base_de_telas_e_login.Loja.Loja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto_base_de_telas_e_login.Loja.LojaBairro.LojaBairro;

import java.util.Optional;

@Repository
public interface LojaRepository extends JpaRepository<Loja, Long> {

    Optional<Loja> findByNomeLoja(String nomeLoja);
}