package projeto_base_de_telas_e_login.Pedido;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;

import java.time.LocalDateTime;
import java.util.List;

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