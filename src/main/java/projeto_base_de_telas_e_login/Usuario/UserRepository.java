package projeto_base_de_telas_e_login.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
        Optional<UserEntity> findById(UUID id);

        Optional<UserEntity> findByUsername(String username);

    }

