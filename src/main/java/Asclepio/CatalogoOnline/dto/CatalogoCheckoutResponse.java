package Asclepio.CatalogoOnline.dto;

import Asclepio.Loja.FormaPagamento.LojaFormaPagamento;
import Asclepio.Loja.LojaBairro.LojaBairro;
import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Enum.TipoAtendimentoPedido;

import java.math.BigDecimal;
import java.util.List;

// O que o cliente pode escolher ao finalizar o pedido nessa loja.
public record CatalogoCheckoutResponse(
        Long lojaId,
        String nomeLoja,
        List<TipoAtendimentoPedido> tiposAtendimento,
        List<FormaPagamento> formasPagamento,
        List<Bairro> bairros,
        BigDecimal valorMinimoFreteGratis
) {

    public record FormaPagamento(
            FormaDePagamento forma,
            String descricao
    ) {
        public static FormaPagamento fromEntity(LojaFormaPagamento forma) {
            return new FormaPagamento(
                    forma.getFormaPagamento(),
                    forma.getFormaPagamento().getDescricao()
            );
        }
    }

    public record Bairro(
            Long bairroId,
            String nomeBairro,
            BigDecimal valorFrete
    ) {
        public static Bairro fromEntity(LojaBairro lojaBairro) {
            return new Bairro(
                    lojaBairro.getBairro().getId(),
                    lojaBairro.getBairro().getNome(),
                    lojaBairro.getValorFrete()
            );
        }
    }
}
