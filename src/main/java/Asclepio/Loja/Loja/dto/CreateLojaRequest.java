package Asclepio.Loja.Loja.dto;

import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;

import java.math.BigDecimal;

public record CreateLojaRequest(
        String nomeLoja,
        TipoAtendimentoLoja tipoAtendimento,
        BigDecimal valorMinimoFreteGratis,
        String cep,
        String cpnj,
        String telefone,
        String TextoDescricao,
        String imagemUrl

) {

}