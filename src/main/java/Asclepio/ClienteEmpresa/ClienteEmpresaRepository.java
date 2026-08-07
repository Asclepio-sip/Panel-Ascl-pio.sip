package Asclepio.ClienteEmpresa;

import Asclepio.ClienteEmpresa.ClienteEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteEmpresaRepository extends JpaRepository<ClienteEmpresa, UUID> {

    List<ClienteEmpresa> findAllByEmpresa_IdOrderByNomeAsc(Long empresaId);

    Optional<ClienteEmpresa> findByIdAndEmpresa_Id(UUID id, Long empresaId);
}