package Asclepio.Dashboard;

import Asclepio.Dashboard.dto.ItemVendido;
import Asclepio.ItemPedido.ItemPedido;
import Asclepio.Pedido.Enum.StatusDoPedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/** Consultas de leitura do dashboard (itens vendidos no período, sem pedidos cancelados). */
public interface DashboardRepository extends Repository<ItemPedido, Long> {

    @Query("""
            select new Asclepio.Dashboard.dto.ItemVendido(
                p.id, p.criadoEm, p.loja.id, i.variacaoId, i.quantidade, i.subtotal, i.custoUnitario,
                i.produtoId, i.nomeProduto, c.id, c.nome, p.nomeCliente, p.telefone, p.email)
            from ItemPedido i
            join i.pedido p
            left join p.cliente c
            where p.empresa.id = :empresaId
              and p.criadoEm >= :inicio
              and p.criadoEm < :fim
              and p.status <> :cancelado
            """)
    List<ItemVendido> itensVendidos(@Param("empresaId") Long empresaId,
                                    @Param("inicio") LocalDateTime inicio,
                                    @Param("fim") LocalDateTime fim,
                                    @Param("cancelado") StatusDoPedido cancelado);

    @Query("""
            select new Asclepio.Dashboard.dto.ItemVendido(
                p.id, p.criadoEm, p.loja.id, i.variacaoId, i.quantidade, i.subtotal, i.custoUnitario,
                i.produtoId, i.nomeProduto, c.id, c.nome, p.nomeCliente, p.telefone, p.email)
            from ItemPedido i
            join i.pedido p
            left join p.cliente c
            where p.empresa.id = :empresaId
              and p.loja.id = :lojaId
              and p.criadoEm >= :inicio
              and p.criadoEm < :fim
              and p.status <> :cancelado
            """)
    List<ItemVendido> itensVendidosDaLoja(@Param("empresaId") Long empresaId,
                                          @Param("lojaId") Long lojaId,
                                          @Param("inicio") LocalDateTime inicio,
                                          @Param("fim") LocalDateTime fim,
                                          @Param("cancelado") StatusDoPedido cancelado);
}
