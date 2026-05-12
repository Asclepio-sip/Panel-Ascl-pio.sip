package projeto_base_de_telas_e_login.Loja.LojaBairro.dto;

import java.math.BigDecimal;

public record CreateLojaBairroRequest (
        Long lojaId,
        String lojaNome,
        Long bairroId,
        String bairroNom,
        BigDecimal valorFrete

){
}