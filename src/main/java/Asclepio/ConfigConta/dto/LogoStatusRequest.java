package Asclepio.ConfigConta.dto;

import jakarta.validation.constraints.NotNull;

public record LogoStatusRequest(
        @NotNull(message = "Informe se a logo está ativa")
        Boolean ativo
) {
}
