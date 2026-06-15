package Asclepio.Loja.Loja.dto;

import Asclepio.Loja.Bairro.Enum.TipoAtendimentoLoja;

public record LojaFiltroDTO(

        Long id,

        String nomeLoja,

        String cep,

        String cnpj,

        String telefone,

        TipoAtendimentoLoja tipoAtendimento

) {
}