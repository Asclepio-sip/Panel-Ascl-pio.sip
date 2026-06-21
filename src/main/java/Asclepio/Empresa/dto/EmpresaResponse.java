package Asclepio.Empresa.dto;

import Asclepio.Empresa.Empresa;

import java.time.LocalDateTime;

public record EmpresaResponse(
        Long id,
        String nome,
        String cnpj,
        Boolean ativa,
        LocalDateTime criadoEm
) {

    public static EmpresaResponse fromEntity(Empresa empresa) {
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getAtiva(),
                empresa.getCriadoEm()
        );
    }
}