package Asclepio.ConfigConta.dto;

import Asclepio.ConfigConta.ConfigConta;

public record LogoResponse(
        String logoUrl,
        Boolean logoAtivo
) {

    public static LogoResponse fromEntity(ConfigConta config) {
        return new LogoResponse(
                config.getLogoUrl(),
                config.getLogoAtivo()
        );
    }
}
