package Asclepio.Plano;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Long> {

    List<Plano> findAllByOrderByPrecoAscSaasIdAsc();

    Optional<Plano> findBySaasId(Long saasId);

    List<Plano> findByMaisPopularTrue();
}
