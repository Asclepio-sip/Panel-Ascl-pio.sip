package Asclepio.Dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/** Linha de item vendido usada só para montar o dashboard (projeção da consulta). */
public record ItemVendido(
        Long pedidoId,
        LocalDateTime criadoEm,
        Long lojaId,
        Long variacaoId,
        Integer quantidade,
        BigDecimal subtotal,
        BigDecimal custoUnitario,
        Long produtoId,
        String nomeProduto,
        /* Cliente cadastrado (null em pedido sem cliente vinculado, ex.: catálogo) */
        UUID clienteId,
        String nomeClienteCadastro,
        /* Dados do cliente gravados no pedido */
        String nomeCliente,
        String telefone,
        String email
) {
}
