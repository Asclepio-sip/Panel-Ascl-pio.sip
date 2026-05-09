package projeto_base_de_telas_e_login.adapter.out.persistence.Pedido;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.StatusDoPedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    // Corrigido: criadoEm
    List<PedidoEntity> findByCriadoEmBetweenOrderByCriadoEmDesc(
            LocalDateTime inicio,
            LocalDateTime fim
    );

    @Modifying
    @Transactional
    @Query("UPDATE PedidoEntity p SET p.status = :status WHERE p.id = :id")
    void atualizarStatus(@Param("id") Long id, @Param("status") StatusDoPedido status);
}