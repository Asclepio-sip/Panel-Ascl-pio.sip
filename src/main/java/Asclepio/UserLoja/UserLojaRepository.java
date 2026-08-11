package Asclepio.UserLoja;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Usuario.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLojaRepository extends JpaRepository<UserLoja, Long> {

    List<UserLoja> findAllByUser(User user);

    Optional<UserLoja> findByUserAndLoja(User user, Loja loja);

    Optional<UserLoja> findByUser_IdAndLoja_Id(UUID userId, Long lojaId);

    boolean existsByUser_IdAndLoja_Empresa_Id(UUID userId, Long empresaId);

    boolean existsByUser_IdAndLoja_Id(UUID userId, Long lojaId);


}