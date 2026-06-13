package projeto_base_de_telas_e_login.Pedido.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto_base_de_telas_e_login.Pedido.Pedido;
import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    List<Pedido> findByCriadoEmBetweenOrderByCriadoEmDesc(LocalDateTime inicio, LocalDateTime fim);

    @Modifying
    @Query("""
            update Pedido p
            set p.status = :status
            where p.id = :id
            """)
    void atualizarStatus(@Param("id") Long id, @Param("status") StatusDoPedido status);
}