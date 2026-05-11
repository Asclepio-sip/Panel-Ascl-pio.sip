package projeto_base_de_telas_e_login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import projeto_base_de_telas_e_login.entidade.Pedido;
import projeto_base_de_telas_e_login.entidade.Product;
import projeto_base_de_telas_e_login.entidade.UserEntity;
import projeto_base_de_telas_e_login.entidade.Categoria;
import projeto_base_de_telas_e_login.model.Pedido.Enum.StatusDoPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNomeCategoria(String nomeCategoria);

    void deleteById(Long id);

    interface UserRepository extends JpaRepository<UserEntity, UUID> {
        Optional<UserEntity> findById(UUID id);

        Optional<UserEntity> findByUsername(String username);

    }

    interface PedidoRepository extends JpaRepository<Pedido, Long> {

        List<Pedido> findByCriadoEmBetweenOrderByCriadoEmDesc(LocalDateTime inicio, LocalDateTime fim);

        @Modifying
        @Transactional
        @Query("""
                    UPDATE Pedido p
                    SET p.status = :status
                    WHERE p.id = :id
                """)
        void atualizarStatus(@Param("id") Long id, @Param("status") StatusDoPedido status);
    }

    interface ProductRepository extends JpaRepository<Product, Long> {

        List<Product> findByCategoria_NomeCategoria(String nomeCategoria);

        Optional<Product> findByName(String name);
    }
}
