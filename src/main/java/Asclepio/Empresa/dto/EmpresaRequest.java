package Asclepio.Empresa.dto;

public record EmpresaRequest(
        String nome,
        String cnpj,
        Boolean ativa
) {
}