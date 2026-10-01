package Asclepio.ConfigConta.dto;

import jakarta.validation.constraints.NotNull;

public record BannerStatusRequest(
        @NotNull(message = "Informe se o banner está ativo")
        Boolean ativo
) {
}
