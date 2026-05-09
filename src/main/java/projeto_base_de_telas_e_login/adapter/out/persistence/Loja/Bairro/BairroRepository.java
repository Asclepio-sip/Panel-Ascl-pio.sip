package projeto_base_de_telas_e_login.adapter.out.persistence.Loja.Bairro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto_base_de_telas_e_login.domain.model.Loja.Bairro;

import java.util.Optional;

@Repository
public interface BairroRepository extends JpaRepository<BairroEntity, Long> {
    Optional<BairroEntity> findByNome(String nome);

}