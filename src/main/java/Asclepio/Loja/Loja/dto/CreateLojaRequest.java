package Asclepio.Loja.Loja.dto;

import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;

import java.math.BigDecimal;

public record CreateLojaRequest(
        String nomeLoja,
        String cep,
        String cnpj,
        String telefone,
        String TextoDescricao,
        String imagemUrl,
        TipoAtendimentoLoja tipoAtendimento,
        BigDecimal valorMinimoFreteGratis,
        Long empresaId

) {
}