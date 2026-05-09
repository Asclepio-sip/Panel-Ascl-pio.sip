package projeto_base_de_telas_e_login.dto.loja.lojabairros;

import java.math.BigDecimal;

public record CreateLojaBairroRequest (
        Long lojaId,
        String lojaNome,
        Long bairroId,
        String bairroNom,
        BigDecimal valorFrete

){
}