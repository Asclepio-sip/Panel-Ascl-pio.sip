package Asclepio.Estoque.dto;

import Asclepio.Estoque.Estoque;
import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ListaDeEstoqueDasLojasResponse(

        Long id,
        Long lojaId,
        String nomeLoja,
        Long produtoId,
        String imagemBase64,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoVenda,
        BigDecimal percentualDesconto,
        BigDecimal valorFinal,
        String Variacao,
        String Entregar

) {

    public static ListaDeEstoqueDasLojasResponse fromDomain(Estoque estoque) {

        String Entregar;

        BigDecimal desconto = estoque.getPercentualDesconto() != null
                ? estoque.getPercentualDesconto()
                : BigDecimal.ZERO;

        BigDecimal valorDesconto = estoque.getPrecoVenda()
                .multiply(desconto)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal valorFinal = estoque.getPrecoVenda()
                .subtract(valorDesconto);

        if (estoque.getLoja().getTipoAtendimento() == TipoAtendimentoLoja.ENTREGA ||estoque.getLoja().getTipoAtendimento() == TipoAtendimentoLoja.AMBOS  ){
            Entregar = "ENTREGA";
        }else {
            Entregar = "RETIRADA";
        }


        var variacao = estoque.getProdutoVariacao();

        var produto = variacao.getProduto();


        return new ListaDeEstoqueDasLojasResponse(

                estoque.getId(),

                estoque.getLoja().getId(),

                estoque.getLoja().getNomeLoja(),

                produto.getId(),

                produto.getImagemBase64(),

                produto.getName(),

                estoque.getQuantidade(),

                estoque.getPrecoVenda(),

                desconto,

                valorFinal,

                variacao.getNomeVariacao(),

                Entregar

        );
    }
}