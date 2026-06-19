package Asclepio.Loja.LojaBairro.dto;

import java.math.BigDecimal;

public record CreateLojaBairroRequest (
        Long lojaId,
        String lojaNome,
        Long bairroId,
        String bairroNom,
        BigDecimal valorFrete

){
}