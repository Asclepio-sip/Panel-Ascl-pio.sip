package Asclepio.ClienteEmpresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ClienteEmpresaRepository
        extends JpaRepository<ClienteEmpresa, UUID>, JpaSpecificationExecutor<ClienteEmpresa> {

    Optional<ClienteEmpresa> findByIdAndEmpresa_Id(UUID id, Long empresaId);
}