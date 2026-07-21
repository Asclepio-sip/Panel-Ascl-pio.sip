package Asclepio.Usuario.Role;

import Asclepio.Empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByNomeAndEmpresa(String nome, Empresa empresa);

    Optional<Role> findByIdAndEmpresa_Id(UUID id, Long empresaId);
}