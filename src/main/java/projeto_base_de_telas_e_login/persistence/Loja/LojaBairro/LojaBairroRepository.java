package projeto_base_de_telas_e_login.persistence.Loja.LojaBairro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LojaBairroRepository
        extends JpaRepository<LojaBairro, Long> {

    List<LojaBairro> findByLoja_Id(Long lojaId);
}