package Asclepio.ConfigConta.dto;

import Asclepio.ConfigConta.ConfigConta;

import java.time.LocalDateTime;

public record ConfigContaResponse(
        Long id,
        Long empresaId,
        Boolean catalogoOnlineAtivo,
        String nomeLink,
        LocalDateTime atualizadoEm
) {

    public static ConfigContaResponse fromEntity(ConfigConta config) {
        return new ConfigContaResponse(
                config.getId(),
                config.getEmpresa().getId(),
                config.getCatalogoOnlineAtivo(),
                config.getNomeLink(),
                config.getAtualizadoEm()
        );
    }
}
