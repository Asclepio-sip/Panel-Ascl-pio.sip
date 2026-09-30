package Asclepio.ConfigConta.dto;

import jakarta.validation.constraints.NotNull;

public record CatalogoOnlineRequest(
        @NotNull(message = "Informe se o catálogo online está ativo")
        Boolean ativo
) {
}
