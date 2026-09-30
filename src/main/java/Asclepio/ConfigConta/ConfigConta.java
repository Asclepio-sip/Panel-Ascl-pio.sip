package Asclepio.ConfigConta;

import Asclepio.ConfigConta.Enum.ModeloCardCatalogo;
import Asclepio.ConfigConta.Enum.ModeloNavbarCatalogo;
import Asclepio.ConfigConta.dto.DesignCatalogoRequest;
import Asclepio.Empresa.Empresa;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_CONFIG_CONTA",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CFG_EMPRESA",
                        columnNames = "CFG_EMPRESA_ID"),
                @UniqueConstraint(
                        name = "UK_CFG_NOME_LINK",
                        columnNames = "CFG_NOME_LINK")
        })
public class ConfigConta {

    public static final String COR_PRIMARIA_PADRAO = "#2563EB";
    public static final String COR_SECUNDARIA_PADRAO = "#1F2937";
    public static final String COR_FUNDO_PADRAO = "#FFFFFF";
    public static final String COR_NAVBAR_PADRAO = "#FFFFFF";
    public static final String COR_TEXTO_NAVBAR_PADRAO = "#1F2937";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CFG_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CFG_EMPRESA_ID", nullable = false)
    private Empresa empresa;

    @Column(name = "CFG_CATALOGO_ONLINE_ATIVO", nullable = false)
    private Boolean catalogoOnlineAtivo = false;

    // Usado na URL pública do catálogo: /catalogo-online/{nomeLink}/...
    @Column(name = "CFG_NOME_LINK", length = 60)
    private String nomeLink;

    // ===== Design do catálogo =====

    @Enumerated(EnumType.STRING)
    @Column(name = "CFG_MODELO_CARD", length = 30)
    private ModeloCardCatalogo modeloCard = ModeloCardCatalogo.PADRAO;

    @Enumerated(EnumType.STRING)
    @Column(name = "CFG_MODELO_NAVBAR", length = 40)
    private ModeloNavbarCatalogo modeloNavbar = ModeloNavbarCatalogo.PADRAO;

    @Column(name = "CFG_DESCRICAO_RODAPE", columnDefinition = "TEXT")
    private String descricaoRodape;

    @Column(name = "CFG_WHATSAPP", length = 13)
    private String whatsapp;

    @Column(name = "CFG_INSTAGRAM", length = 150)
    private String instagram;

    @Column(name = "CFG_FACEBOOK", length = 150)
    private String facebook;

    @Column(name = "CFG_TIKTOK", length = 150)
    private String tiktok;

    @Column(name = "CFG_EMAIL_SUPORTE", length = 150)
    private String emailSuporte;

    @Column(name = "CFG_TELEFONE_SUPORTE", length = 13)
    private String telefoneSuporte;

    // Liga/desliga o botão flutuante do WhatsApp no catálogo (usa o número de "whatsapp")
    @Column(name = "CFG_MOSTRAR_BOTAO_WHATSAPP")
    private Boolean mostrarBotaoWhatsapp = false;

    // Liga/desliga os ícones das redes sociais no rodapé
    @Column(name = "CFG_MOSTRAR_ICONES_REDES")
    private Boolean mostrarIconesRedesSociais = true;

    // Cores do site em hexadecimal (#RRGGBB)
    @Column(name = "CFG_COR_PRIMARIA", length = 7)
    private String corPrimaria = COR_PRIMARIA_PADRAO;

    @Column(name = "CFG_COR_SECUNDARIA", length = 7)
    private String corSecundaria = COR_SECUNDARIA_PADRAO;

    @Column(name = "CFG_COR_FUNDO", length = 7)
    private String corFundo = COR_FUNDO_PADRAO;

    @Column(name = "CFG_COR_NAVBAR", length = 7)
    private String corNavbar = COR_NAVBAR_PADRAO;

    @Column(name = "CFG_COR_TEXTO_NAVBAR", length = 7)
    private String corTextoNavbar = COR_TEXTO_NAVBAR_PADRAO;

    // ===== Logo do catálogo =====

    @Column(name = "CFG_LOGO_URL", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(name = "CFG_LOGO_ATIVO")
    private Boolean logoAtivo = false;

    // ===== Banner do catálogo =====

    @Column(name = "CFG_BANNER_URL", columnDefinition = "TEXT")
    private String bannerUrl;

    @Column(name = "CFG_BANNER_ATIVO")
    private Boolean bannerAtivo = false;

    @Column(name = "CFG_ATUALIZADO_EM", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected ConfigConta() {
    }

    public ConfigConta(Empresa empresa) {
        this.empresa = empresa;
        this.catalogoOnlineAtivo = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public void alterarCatalogoOnline(Boolean ativo) {
        this.catalogoOnlineAtivo = Boolean.TRUE.equals(ativo);
    }

    public void alterarNomeLink(String nomeLink) {
        this.nomeLink = nomeLink;
    }

    public void alterarDesignCatalogo(DesignCatalogoRequest dto) {
        this.modeloCard = dto.modeloCard();
        this.modeloNavbar = dto.modeloNavbar() != null ? dto.modeloNavbar() : ModeloNavbarCatalogo.PADRAO;
        this.descricaoRodape = tratarTexto(dto.descricaoRodape());
        this.whatsapp = tratarTexto(dto.whatsapp());
        this.instagram = tratarTexto(dto.instagram());
        this.facebook = tratarTexto(dto.facebook());
        this.tiktok = tratarTexto(dto.tiktok());
        this.emailSuporte = tratarTexto(dto.emailSuporte());
        this.telefoneSuporte = tratarTexto(dto.telefoneSuporte());
        this.mostrarBotaoWhatsapp = Boolean.TRUE.equals(dto.mostrarBotaoWhatsapp());
        this.mostrarIconesRedesSociais = !Boolean.FALSE.equals(dto.mostrarIconesRedesSociais());
        this.corPrimaria = corOuPadrao(dto.corPrimaria(), COR_PRIMARIA_PADRAO);
        this.corSecundaria = corOuPadrao(dto.corSecundaria(), COR_SECUNDARIA_PADRAO);
        this.corFundo = corOuPadrao(dto.corFundo(), COR_FUNDO_PADRAO);
        this.corNavbar = corOuPadrao(dto.corNavbar(), COR_NAVBAR_PADRAO);
        this.corTextoNavbar = corOuPadrao(dto.corTextoNavbar(), COR_TEXTO_NAVBAR_PADRAO);
    }

    private String corOuPadrao(String cor, String padrao) {
        return cor == null || cor.isBlank() ? padrao : cor.trim().toUpperCase();
    }

    public void alterarLogo(String logoUrl) {
        this.logoUrl = logoUrl;
        this.logoAtivo = true;
    }

    public void alterarLogoAtivo(Boolean ativo) {
        this.logoAtivo = Boolean.TRUE.equals(ativo);
    }

    public boolean possuiLogo() {
        return logoUrl != null && !logoUrl.isBlank();
    }

    // O que o catálogo público mostra: só com imagem enviada e logo ativa
    public String getLogoUrlVisivel() {
        return possuiLogo() && Boolean.TRUE.equals(logoAtivo) ? logoUrl : null;
    }

    private String tratarTexto(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    public void alterarBanner(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        this.bannerAtivo = true;
    }

    public void alterarBannerAtivo(Boolean ativo) {
        this.bannerAtivo = Boolean.TRUE.equals(ativo);
    }

    public boolean possuiBanner() {
        return bannerUrl != null && !bannerUrl.isBlank();
    }

    // O que o catálogo público mostra: só com imagem enviada e banner ativo
    public String getBannerUrlVisivel() {
        return possuiBanner() && Boolean.TRUE.equals(bannerAtivo) ? bannerUrl : null;
    }

    public boolean possuiNomeLink() {
        return nomeLink != null && !nomeLink.isBlank();
    }

    public boolean catalogoOnlineEstaAtivo() {
        return Boolean.TRUE.equals(catalogoOnlineAtivo);
    }

    public Long getId() {
        return id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Boolean getCatalogoOnlineAtivo() {
        return catalogoOnlineAtivo;
    }

    public String getNomeLink() {
        return nomeLink;
    }

    // Registros antigos (antes da coluna existir) podem vir null: tratados como PADRAO
    public ModeloCardCatalogo getModeloCard() {
        return modeloCard != null ? modeloCard : ModeloCardCatalogo.PADRAO;
    }

    public ModeloNavbarCatalogo getModeloNavbar() {
        return modeloNavbar != null ? modeloNavbar : ModeloNavbarCatalogo.PADRAO;
    }

    public String getDescricaoRodape() {
        return descricaoRodape;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTiktok() {
        return tiktok;
    }

    public String getEmailSuporte() {
        return emailSuporte;
    }

    public String getTelefoneSuporte() {
        return telefoneSuporte;
    }

    // Colunas novas podem vir null em registros antigos: tratadas com o valor padrão
    public Boolean getMostrarBotaoWhatsapp() {
        return Boolean.TRUE.equals(mostrarBotaoWhatsapp);
    }

    public Boolean getMostrarIconesRedesSociais() {
        return !Boolean.FALSE.equals(mostrarIconesRedesSociais);
    }

    public String getCorPrimaria() {
        return corPrimaria != null ? corPrimaria : COR_PRIMARIA_PADRAO;
    }

    public String getCorSecundaria() {
        return corSecundaria != null ? corSecundaria : COR_SECUNDARIA_PADRAO;
    }

    public String getCorFundo() {
        return corFundo != null ? corFundo : COR_FUNDO_PADRAO;
    }

    public String getCorNavbar() {
        return corNavbar != null ? corNavbar : COR_NAVBAR_PADRAO;
    }

    public String getCorTextoNavbar() {
        return corTextoNavbar != null ? corTextoNavbar : COR_TEXTO_NAVBAR_PADRAO;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Boolean getLogoAtivo() {
        return Boolean.TRUE.equals(logoAtivo);
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public Boolean getBannerAtivo() {
        return Boolean.TRUE.equals(bannerAtivo);
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
