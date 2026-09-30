package Asclepio.ConfigConta.dto;

import Asclepio.ConfigConta.ConfigConta;

public record BannerResponse(
        String bannerUrl,
        Boolean bannerAtivo
) {

    public static BannerResponse fromEntity(ConfigConta config) {
        return new BannerResponse(
                config.getBannerUrl(),
                config.getBannerAtivo()
        );
    }
}
