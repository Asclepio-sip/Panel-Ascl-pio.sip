package projeto_base_de_telas_e_login.Loja.LojaBairro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LojaBairroRepository extends JpaRepository<LojaBairro, Long> {

    List<LojaBairro> findByLoja_Id(Long lojaId);

    Optional<LojaBairro> findByLoja_IdAndBairro_Id(Long lojaId, Long bairroId);
}