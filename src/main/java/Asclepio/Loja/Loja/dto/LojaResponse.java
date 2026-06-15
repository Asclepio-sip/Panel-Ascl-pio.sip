package Asclepio.Loja.Loja.dto;

import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;
import Asclepio.Loja.Loja.Loja;

import java.math.BigDecimal;

public record LojaResponse(

        Long id,
        String nomeLoja,
        TipoAtendimentoLoja tipoAtendimento,
        BigDecimal valorMinimoFreteGratis,
        String cep,
        String cpnj,
        String telefone,
        String TextoDescricao
) {


    public static LojaResponse fromEntity(
            Loja loja
    ) {

        return new LojaResponse(
                loja.getId(),
                loja.getNomeLoja(),
                loja.getTipoAtendimento(),
                loja.getValorMinimoFreteGratis(),
                loja.getCep(),
                loja.getCnpj(),
                loja.getTelefone(),
                loja.getTextoDescricao()

        );
    }
}