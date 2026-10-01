package Asclepio.ConfigConta.controller.api;

import Asclepio.ConfigConta.dto.BannerResponse;
import Asclepio.ConfigConta.dto.BannerStatusRequest;
import Asclepio.ConfigConta.dto.CatalogoOnlineRequest;
import Asclepio.ConfigConta.dto.ConfigContaResponse;
import Asclepio.ConfigConta.dto.DesignCatalogoRequest;
import Asclepio.ConfigConta.dto.DesignCatalogoResponse;
import Asclepio.ConfigConta.dto.LogoResponse;
import Asclepio.ConfigConta.dto.LogoStatusRequest;
import Asclepio.ConfigConta.dto.NomeLinkRequest;
import Asclepio.ConfigConta.dto.NomeLinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/config-conta")
@Tag(name = "Configuração da conta", description = "Configurações da empresa (ex.: catálogo online)")
public interface ConfigContaApi {

    @Operation(summary = "Buscar configurações da empresa logada", description = """
            Retorna as configurações da empresa do token.
            Se a empresa ainda não tiver configuração, ela é criada com tudo desativado.
            """)
    @ApiResponse(responseCode = "200", description = "Configurações da empresa")
    @GetMapping
    @PreAuthorize("hasAuthority('VerConfigConta')")
    ResponseEntity<ConfigContaResponse> buscar();

    @Operation(summary = "Ativar/desativar catálogo online", description = """
            Liga ou desliga o catálogo online público da empresa.
            
            Com o catálogo ativo, qualquer pessoa (sem login) consegue listar os produtos
            em estoque das lojas da empresa em GET /catalogo-online/{nomeLink}/lojas/{lojaId}/produtos.
            
            Exemplo de body:
            { "ativo": true }
            """)
    @ApiResponse(responseCode = "200", description = "Configuração atualizada")
    @PatchMapping("/catalogo-online")
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<ConfigContaResponse> alterarCatalogoOnline(@RequestBody @Valid CatalogoOnlineRequest dto);

    @Operation(summary = "Buscar nome do link do catálogo", description = """
            Retorna o nome usado na URL pública do catálogo da empresa logada.
            404 se ainda não foi definido.
            """)
    @ApiResponse(responseCode = "200", description = "Nome do link")
    @GetMapping("/nome-link")
    @PreAuthorize("hasAuthority('VerConfigConta')")
    ResponseEntity<NomeLinkResponse> buscarNomeLink();

    @Operation(summary = "Definir nome do link do catálogo", description = """
            Define pela primeira vez o nome usado na URL pública do catálogo.
            Se já existir, use o PUT.
            
            Regras: 3 a 60 caracteres, só letras minúsculas, números e hífen,
            sem hífen no começo/fim. Precisa ser único entre todas as empresas.
            
            Exemplo de body:
            { "nomeLink": "farmacia-central" }
            """)
    @ApiResponse(responseCode = "201", description = "Nome do link definido")
    @ApiResponse(responseCode = "400", description = "Formato inválido, já definido ou já em uso")
    @PostMapping("/nome-link")
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<NomeLinkResponse> criarNomeLink(@RequestBody @Valid NomeLinkRequest dto);

    @Operation(summary = "Alterar nome do link do catálogo", description = """
            Troca o nome usado na URL pública do catálogo.
            Atenção: links antigos divulgados param de funcionar.
            
            Mesmas regras do POST.
            """)
    @ApiResponse(responseCode = "200", description = "Nome do link alterado")
    @ApiResponse(responseCode = "400", description = "Formato inválido ou já em uso")
    @ApiResponse(responseCode = "404", description = "Nome do link ainda não foi definido")
    @PutMapping("/nome-link")
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<NomeLinkResponse> alterarNomeLink(@RequestBody @Valid NomeLinkRequest dto);

    @Operation(summary = "Buscar design do catálogo", description = """
            Retorna o modelo de card, a descrição do rodapé e os contatos/redes sociais
            usados no catálogo online da empresa logada.
            """)
    @ApiResponse(responseCode = "200", description = "Design do catálogo")
    @GetMapping("/design-catalogo")
    @PreAuthorize("hasAuthority('VerConfigConta')")
    ResponseEntity<DesignCatalogoResponse> buscarDesignCatalogo();

    @Operation(summary = "Salvar design do catálogo", description = """
            Salva o design do catálogo inteiro de uma vez (campo não enviado ou vazio = apagado).
            
            modeloCard (obrigatório): PADRAO, RETANGULAR, GRANDE ou COMPACTO.
            modeloNavbar (null = PADRAO): PADRAO, LOGO_BUSCA_CATEGORIAS_HORIZONTAL
              (logo + busca, categorias na horizontal embaixo) ou LOGO_BUSCA_BOTAO_CATEGORIAS
              (logo + busca + botão que abre as categorias).
            whatsapp / telefoneSuporte: só números com DDD (10 a 13 dígitos).
            instagram / facebook / tiktok: @usuario ou link, até 150 caracteres.
            descricaoRodape: até 1000 caracteres.
            mostrarBotaoWhatsapp: botão flutuante do WhatsApp (null = desligado; ligado exige whatsapp).
            mostrarIconesRedesSociais: ícones das redes no rodapé (null = ligado).
            corPrimaria / corSecundaria / corFundo: #RRGGBB (null ou vazio = cor padrão).
            corNavbar / corTextoNavbar: fundo e texto da navbar, #RRGGBB (null ou vazio = #FFFFFF / #1F2937).
            
            Exemplo de body:
            {
              "modeloCard": "GRANDE",
              "modeloNavbar": "LOGO_BUSCA_CATEGORIAS_HORIZONTAL",
              "descricaoRodape": "Farmácia de bairro desde 1998.",
              "whatsapp": "11999998888",
              "instagram": "@farmaciacentral",
              "facebook": null,
              "tiktok": null,
              "emailSuporte": "contato@farmaciacentral.com",
              "telefoneSuporte": "1133334444",
              "mostrarBotaoWhatsapp": true,
              "mostrarIconesRedesSociais": true,
              "corPrimaria": "#2563EB",
              "corSecundaria": "#1F2937",
              "corFundo": "#FFFFFF",
              "corNavbar": "#FFFFFF",
              "corTextoNavbar": "#1F2937"
            }
            """)
    @ApiResponse(responseCode = "200", description = "Design salvo")
    @ApiResponse(responseCode = "400", description = "Campo inválido")
    @PutMapping("/design-catalogo")
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<DesignCatalogoResponse> alterarDesignCatalogo(@RequestBody @Valid DesignCatalogoRequest dto);

    @Operation(summary = "Buscar logo do catálogo", description = """
            Retorna a URL da logo e se ela está ativa.
            logoUrl = null quando ainda não foi enviada.
            """)
    @ApiResponse(responseCode = "200", description = "Logo do catálogo")
    @GetMapping("/logo")
    @PreAuthorize("hasAuthority('VerConfigConta')")
    ResponseEntity<LogoResponse> buscarLogo();

    @Operation(summary = "Enviar logo do catálogo", description = """
            Envia a logo pela primeira vez (multipart/form-data, campo "imagem").
            A logo já fica ativa. Se já existir, use o PUT.
            
            A imagem é redimensionada para no máximo 512px no maior lado.
            Transparência não é mantida: fundo transparente vira branco.
            """)
    @ApiResponse(responseCode = "201", description = "Logo enviada")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou logo já enviada")
    @PostMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<LogoResponse> criarLogo(@RequestParam MultipartFile imagem);

    @Operation(summary = "Trocar logo do catálogo", description = """
            Troca a logo (multipart/form-data, campo "imagem").
            A imagem antiga é apagada do storage e a logo volta a ficar ativa.
            """)
    @ApiResponse(responseCode = "200", description = "Logo trocada")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido")
    @ApiResponse(responseCode = "404", description = "Logo ainda não foi enviada")
    @PutMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<LogoResponse> trocarLogo(@RequestParam MultipartFile imagem);

    @Operation(summary = "Ativar/desativar logo do catálogo", description = """
            Liga ou desliga a logo sem apagar a imagem.
            Desativada, o catálogo público recebe logoUrl = null.
            
            Exemplo de body:
            { "ativo": false }
            """)
    @ApiResponse(responseCode = "200", description = "Status da logo alterado")
    @ApiResponse(responseCode = "404", description = "Logo ainda não foi enviada")
    @PatchMapping("/logo/status")
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<LogoResponse> alterarLogoAtivo(@RequestBody @Valid LogoStatusRequest dto);

    @Operation(summary = "Buscar banner do catálogo", description = """
            Retorna a URL da imagem do banner e se ele está ativo.
            bannerUrl = null quando ainda não foi enviado.
            """)
    @ApiResponse(responseCode = "200", description = "Banner do catálogo")
    @GetMapping("/banner")
    @PreAuthorize("hasAuthority('VerConfigConta')")
    ResponseEntity<BannerResponse> buscarBanner();

    @Operation(summary = "Enviar banner do catálogo", description = """
            Envia a imagem do banner pela primeira vez (multipart/form-data, campo "imagem").
            O banner já fica ativo. Se já existir, use o PUT.
            
            A imagem é redimensionada para no máximo 1920px no maior lado.
            """)
    @ApiResponse(responseCode = "201", description = "Banner enviado")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou banner já enviado")
    @PostMapping(value = "/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<BannerResponse> criarBanner(@RequestParam MultipartFile imagem);

    @Operation(summary = "Trocar banner do catálogo", description = """
            Troca a imagem do banner (multipart/form-data, campo "imagem").
            A imagem antiga é apagada do storage e o banner volta a ficar ativo.
            """)
    @ApiResponse(responseCode = "200", description = "Banner trocado")
    @ApiResponse(responseCode = "400", description = "Arquivo inválido")
    @ApiResponse(responseCode = "404", description = "Banner ainda não foi enviado")
    @PutMapping(value = "/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<BannerResponse> trocarBanner(@RequestParam MultipartFile imagem);

    @Operation(summary = "Ativar/desativar banner do catálogo", description = """
            Liga ou desliga o banner sem apagar a imagem.
            Desativado, o catálogo público recebe bannerUrl = null.
            
            Exemplo de body:
            { "ativo": false }
            """)
    @ApiResponse(responseCode = "200", description = "Status do banner alterado")
    @ApiResponse(responseCode = "404", description = "Banner ainda não foi enviado")
    @PatchMapping("/banner/status")
    @PreAuthorize("hasAuthority('EditarConfigConta')")
    ResponseEntity<BannerResponse> alterarBannerAtivo(@RequestBody @Valid BannerStatusRequest dto);
}
