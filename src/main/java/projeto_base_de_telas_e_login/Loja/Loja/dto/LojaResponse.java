package projeto_base_de_telas_e_login.Loja.Loja.dto;

import projeto_base_de_telas_e_login.Loja.Bairro.Enum.TipoAtendimentoLoja;
import projeto_base_de_telas_e_login.Loja.Loja.Loja;

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
                loja.getCpnj(),
                loja.getTelefone(),
                loja.getTextoDescricao()

        );
    }
}