package Asclepio.ConfigConta.controller;

import Asclepio.ConfigConta.ConfigContaService;
import Asclepio.ConfigConta.controller.api.ConfigContaApi;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ConfigContaController implements ConfigContaApi {

    private final ConfigContaService service;

    public ConfigContaController(ConfigContaService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ConfigContaResponse> buscar() {
        return ResponseEntity.ok(service.buscarDaEmpresaAtual());
    }

    @Override
    public ResponseEntity<ConfigContaResponse> alterarCatalogoOnline(CatalogoOnlineRequest dto) {
        return ResponseEntity.ok(service.alterarCatalogoOnline(dto));
    }

    @Override
    public ResponseEntity<NomeLinkResponse> buscarNomeLink() {
        return ResponseEntity.ok(service.buscarNomeLink());
    }

    @Override
    public ResponseEntity<NomeLinkResponse> criarNomeLink(NomeLinkRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarNomeLink(dto));
    }

    @Override
    public ResponseEntity<NomeLinkResponse> alterarNomeLink(NomeLinkRequest dto) {
        return ResponseEntity.ok(service.alterarNomeLink(dto));
    }

    @Override
    public ResponseEntity<DesignCatalogoResponse> buscarDesignCatalogo() {
        return ResponseEntity.ok(service.buscarDesignCatalogo());
    }

    @Override
    public ResponseEntity<DesignCatalogoResponse> alterarDesignCatalogo(DesignCatalogoRequest dto) {
        return ResponseEntity.ok(service.alterarDesignCatalogo(dto));
    }

    @Override
    public ResponseEntity<BannerResponse> buscarBanner() {
        return ResponseEntity.ok(service.buscarBanner());
    }

    @Override
    public ResponseEntity<BannerResponse> criarBanner(MultipartFile imagem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarBanner(imagem));
    }

    @Override
    public ResponseEntity<BannerResponse> trocarBanner(MultipartFile imagem) {
        return ResponseEntity.ok(service.trocarBanner(imagem));
    }

    @Override
    public ResponseEntity<BannerResponse> alterarBannerAtivo(BannerStatusRequest dto) {
        return ResponseEntity.ok(service.alterarBannerAtivo(dto));
    }

    @Override
    public ResponseEntity<LogoResponse> buscarLogo() {
        return ResponseEntity.ok(service.buscarLogo());
    }

    @Override
    public ResponseEntity<LogoResponse> criarLogo(MultipartFile imagem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarLogo(imagem));
    }

    @Override
    public ResponseEntity<LogoResponse> trocarLogo(MultipartFile imagem) {
        return ResponseEntity.ok(service.trocarLogo(imagem));
    }

    @Override
    public ResponseEntity<LogoResponse> alterarLogoAtivo(LogoStatusRequest dto) {
        return ResponseEntity.ok(service.alterarLogoAtivo(dto));
    }
}
