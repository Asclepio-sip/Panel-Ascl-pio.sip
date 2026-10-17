package Asclepio.Pedido.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import Asclepio.Pedido.Pedido;
import Asclepio.Pedido.Enum.StatusDoPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    List<Pedido> findByCriadoEmBetweenOrderByCriadoEmDesc(LocalDateTime inicio, LocalDateTime fim);

    @Modifying
    @Query("""
            update Pedido p
            set p.status = :status
            where p.id = :id
            """)
    void atualizarStatus(@Param("id") Long id, @Param("status") StatusDoPedido status);

    boolean existsByCodigoRastreio(String codigoRastreio);

    Optional<Pedido> findByCodigoRastreio(String codigoRastreio);

    Optional<Pedido> findByIdAndEmpresa_Id(Long id, Long empresaId);

    /** Trava a linha do pedido: dois cancelamentos ao mesmo tempo não devolvem o estoque duas vezes. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Pedido> findComLockByIdAndEmpresa_Id(Long id, Long empresaId);

}