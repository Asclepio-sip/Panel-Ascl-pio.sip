package Asclepio.ConfigConta.dto;

import Asclepio.ConfigConta.ConfigConta;
import Asclepio.ConfigConta.Enum.ModeloCardCatalogo;
import Asclepio.ConfigConta.Enum.ModeloNavbarCatalogo;

public record DesignCatalogoResponse(
        ModeloCardCatalogo modeloCard,
        ModeloNavbarCatalogo modeloNavbar,
        String descricaoRodape,
        String whatsapp,
        String instagram,
        String facebook,
        String tiktok,
        String emailSuporte,
        String telefoneSuporte,
        Boolean mostrarBotaoWhatsapp,
        Boolean mostrarIconesRedesSociais,
        String corPrimaria,
        String corSecundaria,
        String corFundo,
        String corNavbar,
        String corTextoNavbar,
        String logoUrl,
        String bannerUrl
) {

    public static DesignCatalogoResponse fromEntity(ConfigConta config) {
        return new DesignCatalogoResponse(
                config.getModeloCard(),
                config.getModeloNavbar(),
                config.getDescricaoRodape(),
                config.getWhatsapp(),
                config.getInstagram(),
                config.getFacebook(),
                config.getTiktok(),
                config.getEmailSuporte(),
                config.getTelefoneSuporte(),
                config.getMostrarBotaoWhatsapp(),
                config.getMostrarIconesRedesSociais(),
                config.getCorPrimaria(),
                config.getCorSecundaria(),
                config.getCorFundo(),
                config.getCorNavbar(),
                config.getCorTextoNavbar(),
                config.getLogoUrlVisivel(),
                config.getBannerUrlVisivel()
        );
    }
}
