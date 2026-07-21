package Asclepio.Usuario.User.Repository;

import Asclepio.Usuario.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import Asclepio.Usuario.User.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndEmpresa_Id(UUID id, Long empresaId);

    Optional<User> findByUsernameAndEmpresa_Id(String username, Long empresaId);
}