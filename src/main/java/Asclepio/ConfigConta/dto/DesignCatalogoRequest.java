package Asclepio.ConfigConta.dto;

import Asclepio.ConfigConta.Enum.ModeloCardCatalogo;
import Asclepio.ConfigConta.Enum.ModeloNavbarCatalogo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DesignCatalogoRequest(

        @NotNull(message = "Informe o modelo do card")
        ModeloCardCatalogo modeloCard,

        // null = PADRAO
        ModeloNavbarCatalogo modeloNavbar,

        @Size(max = 1000, message = "A descrição do rodapé deve ter no máximo 1000 caracteres")
        String descricaoRodape,

        @Pattern(regexp = "^$|^\\d{10,13}$", message = "WhatsApp deve ter só números, com DDD (ex.: 11999998888)")
        String whatsapp,

        @Size(max = 150, message = "Instagram deve ter no máximo 150 caracteres")
        String instagram,

        @Size(max = 150, message = "Facebook deve ter no máximo 150 caracteres")
        String facebook,

        @Size(max = 150, message = "TikTok deve ter no máximo 150 caracteres")
        String tiktok,

        @Email(message = "E-mail de suporte inválido")
        @Size(max = 150, message = "E-mail de suporte deve ter no máximo 150 caracteres")
        String emailSuporte,

        @Pattern(regexp = "^$|^\\d{10,13}$", message = "Telefone de suporte deve ter só números, com DDD")
        String telefoneSuporte,

        // null = desligado
        Boolean mostrarBotaoWhatsapp,

        // null = ligado
        Boolean mostrarIconesRedesSociais,

        // null ou vazio = cor padrão
        @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "Cor primária deve estar no formato #RRGGBB")
        String corPrimaria,

        @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "Cor secundária deve estar no formato #RRGGBB")
        String corSecundaria,

        @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "Cor de fundo deve estar no formato #RRGGBB")
        String corFundo,

        @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "Cor da navbar deve estar no formato #RRGGBB")
        String corNavbar,

        @Pattern(regexp = "^$|^#[0-9A-Fa-f]{6}$", message = "Cor do texto da navbar deve estar no formato #RRGGBB")
        String corTextoNavbar
) {
}
