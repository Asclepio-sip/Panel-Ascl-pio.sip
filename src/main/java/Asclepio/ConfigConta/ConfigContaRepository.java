package Asclepio.ConfigConta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigContaRepository extends JpaRepository<ConfigConta, Long> {

    Optional<ConfigConta> findByEmpresa_Id(Long empresaId);

    Optional<ConfigConta> findByNomeLink(String nomeLink);

    boolean existsByNomeLink(String nomeLink);
}
