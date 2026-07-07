package Asclepio.Estoque.dto;

import Asclepio.Estoque.Estoque;
import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ListaDeEstoqueDasLojasResponse(

        Long id,
        Long lojaId,
        String nomeLoja,
        Long produtoId,
        String imagemUrl,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoVenda,
        BigDecimal percentualDesconto,
        BigDecimal valorFinal,
        String Variacao,
        Long VaricaoId,
        String Entregar

) {

    public static ListaDeEstoqueDasLojasResponse fromDomain(
            Estoque estoque,
            ProdutoVariacaoResponseDTO variacao
    ) {

        String entregar;

        BigDecimal desconto = estoque.getPercentualDesconto() != null
                ? estoque.getPercentualDesconto()
                : BigDecimal.ZERO;

        BigDecimal valorDesconto = estoque.getPrecoVenda()
                .multiply(desconto)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal valorFinal = estoque.getPrecoVenda()
                .subtract(valorDesconto);

        if (
                estoque.getLoja().getTipoAtendimento() == TipoAtendimentoLoja.ENTREGA
                        || estoque.getLoja().getTipoAtendimento() == TipoAtendimentoLoja.AMBOS
        ) {

            entregar = "ENTREGA";

        } else {

            entregar = "RETIRADA";
        }

        return new ListaDeEstoqueDasLojasResponse(

                estoque.getId(),

                estoque.getLoja().getId(),

                estoque.getLoja().getNomeLoja(),

                variacao.produtoId(),

                estoque.getImagemUrl(),

                variacao.nomeProduto(),

                estoque.getQuantidade(),

                estoque.getPrecoVenda(),

                desconto,

                valorFinal,

                variacao.nomeVariacao(),

                variacao.id(),
                entregar
        );
    }

}