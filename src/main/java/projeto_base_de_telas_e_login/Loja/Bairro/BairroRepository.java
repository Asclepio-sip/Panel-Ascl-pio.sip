package projeto_base_de_telas_e_login.Loja.Bairro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BairroRepository extends JpaRepository<Bairro, Long> {
    Optional<Bairro> findByNome(String nome);

}