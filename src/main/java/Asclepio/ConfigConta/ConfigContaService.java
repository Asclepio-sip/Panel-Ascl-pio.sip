package Asclepio.ConfigConta;

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
import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.config.security.StorageService;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.Locale;

@Service
public class ConfigContaService {

    private static final int NOME_LINK_MIN = 3;
    private static final int NOME_LINK_MAX = 60;
    private static final String PASTA_BANNER = "banners";
    private static final int BANNER_TAMANHO_MAXIMO = 1920;
    private static final String PASTA_LOGO = "logos";
    private static final int LOGO_TAMANHO_MAXIMO = 512;

    private final ConfigContaRepository repository;
    private final EmpresaContext empresaContext;
    private final StorageService storageService;

    public ConfigContaService(
            ConfigContaRepository repository,
            EmpresaContext empresaContext,
            StorageService storageService
    ) {
        this.repository = repository;
        this.empresaContext = empresaContext;
        this.storageService = storageService;
    }

    @Transactional
    public ConfigContaResponse buscarDaEmpresaAtual() {
        return ConfigContaResponse.fromEntity(buscarOuCriarDaEmpresaAtual());
    }

    @Transactional
    public ConfigContaResponse alterarCatalogoOnline(CatalogoOnlineRequest dto) {
        ConfigConta config = buscarOuCriarDaEmpresaAtual();
        config.alterarCatalogoOnline(dto.ativo());
        return ConfigContaResponse.fromEntity(repository.save(config));
    }

    // Chamado no cadastro da conta: já nasce com o nome do link gerado a partir do nome da empresa.
    @Transactional
    public ConfigConta criarPadraoParaEmpresa(Empresa empresa) {
        ConfigConta config = new ConfigConta(empresa);
        config.alterarNomeLink(gerarNomeLinkDisponivel(empresa.getNome()));
        return repository.save(config);
    }

    @Transactional(readOnly = true)
    public NomeLinkResponse buscarNomeLink() {
        return repository.findByEmpresa_Id(empresaContext.getEmpresaId())
                .filter(ConfigConta::possuiNomeLink)
                .map(config -> new NomeLinkResponse(config.getNomeLink()))
                .orElseThrow(() -> new ResourceNotFoundException("Nome do link ainda não foi definido"));
    }

    @Transactional
    public NomeLinkResponse criarNomeLink(NomeLinkRequest dto) {
        ConfigConta config = buscarOuCriarDaEmpresaAtual();

        if (config.possuiNomeLink()) {
            throw new BusinessException("O nome do link já foi definido. Use o PUT para alterar");
        }

        return salvarNomeLink(config, dto.nomeLink());
    }

    @Transactional
    public NomeLinkResponse alterarNomeLink(NomeLinkRequest dto) {
        ConfigConta config = repository.findByEmpresa_Id(empresaContext.getEmpresaId())
                .filter(ConfigConta::possuiNomeLink)
                .orElseThrow(() -> new ResourceNotFoundException("Nome do link ainda não foi definido. Use o POST para criar"));

        return salvarNomeLink(config, dto.nomeLink());
    }

    @Transactional
    public DesignCatalogoResponse buscarDesignCatalogo() {
        return DesignCatalogoResponse.fromEntity(buscarOuCriarDaEmpresaAtual());
    }

    @Transactional
    public DesignCatalogoResponse alterarDesignCatalogo(DesignCatalogoRequest dto) {

        boolean semWhatsapp = dto.whatsapp() == null || dto.whatsapp().isBlank();

        if (Boolean.TRUE.equals(dto.mostrarBotaoWhatsapp()) && semWhatsapp) {
            throw new BusinessException("Informe o número do WhatsApp para mostrar o botão flutuante");
        }

        ConfigConta config = buscarOuCriarDaEmpresaAtual();
        config.alterarDesignCatalogo(dto);
        return DesignCatalogoResponse.fromEntity(repository.save(config));
    }

    @Transactional
    public BannerResponse buscarBanner() {
        return BannerResponse.fromEntity(buscarOuCriarDaEmpresaAtual());
    }

    @Transactional
    public BannerResponse criarBanner(MultipartFile imagem) {
        ConfigConta config = buscarOuCriarDaEmpresaAtual();

        if (config.possuiBanner()) {
            throw new BusinessException("O banner já foi enviado. Use o PUT para trocar a imagem");
        }

        config.alterarBanner(enviarImagem(imagem, PASTA_BANNER, BANNER_TAMANHO_MAXIMO, "do banner"));
        return BannerResponse.fromEntity(repository.save(config));
    }

    @Transactional
    public BannerResponse trocarBanner(MultipartFile imagem) {
        ConfigConta config = repository.findByEmpresa_Id(empresaContext.getEmpresaId())
                .filter(ConfigConta::possuiBanner)
                .orElseThrow(() -> new ResourceNotFoundException("Banner ainda não foi enviado. Use o POST para enviar"));

        String bannerAntigo = config.getBannerUrl();

        config.alterarBanner(enviarImagem(imagem, PASTA_BANNER, BANNER_TAMANHO_MAXIMO, "do banner"));
        ConfigConta salva = repository.save(config);

        // Só apaga a imagem antiga do bucket depois que a nova já está salva
        storageService.deletar(bannerAntigo);

        return BannerResponse.fromEntity(salva);
    }

    @Transactional
    public BannerResponse alterarBannerAtivo(BannerStatusRequest dto) {
        ConfigConta config = repository.findByEmpresa_Id(empresaContext.getEmpresaId())
                .filter(ConfigConta::possuiBanner)
                .orElseThrow(() -> new ResourceNotFoundException("Banner ainda não foi enviado"));

        config.alterarBannerAtivo(dto.ativo());
        return BannerResponse.fromEntity(repository.save(config));
    }

    @Transactional
    public LogoResponse buscarLogo() {
        return LogoResponse.fromEntity(buscarOuCriarDaEmpresaAtual());
    }

    @Transactional
    public LogoResponse criarLogo(MultipartFile imagem) {
        ConfigConta config = buscarOuCriarDaEmpresaAtual();

        if (config.possuiLogo()) {
            throw new BusinessException("A logo já foi enviada. Use o PUT para trocar a imagem");
        }

        config.alterarLogo(enviarImagem(imagem, PASTA_LOGO, LOGO_TAMANHO_MAXIMO, "da logo"));
        return LogoResponse.fromEntity(repository.save(config));
    }

    @Transactional
    public LogoResponse trocarLogo(MultipartFile imagem) {
        ConfigConta config = repository.findByEmpresa_Id(empresaContext.getEmpresaId())
                .filter(ConfigConta::possuiLogo)
                .orElseThrow(() -> new ResourceNotFoundException("Logo ainda não foi enviada. Use o POST para enviar"));

        String logoAntiga = config.getLogoUrl();

        config.alterarLogo(enviarImagem(imagem, PASTA_LOGO, LOGO_TAMANHO_MAXIMO, "da logo"));
        ConfigConta salva = repository.save(config);

        // Só apaga a imagem antiga do bucket depois que a nova já está salva
        storageService.deletar(logoAntiga);

        return LogoResponse.fromEntity(salva);
    }

    @Transactional
    public LogoResponse alterarLogoAtivo(LogoStatusRequest dto) {
        ConfigConta config = repository.findByEmpresa_Id(empresaContext.getEmpresaId())
                .filter(ConfigConta::possuiLogo)
                .orElseThrow(() -> new ResourceNotFoundException("Logo ainda não foi enviada"));

        config.alterarLogoAtivo(dto.ativo());
        return LogoResponse.fromEntity(repository.save(config));
    }

    // Usado por rotas públicas: empresa dona do nome do link, só se o catálogo estiver ativo.
    @Transactional(readOnly = true)
    public ConfigConta buscarCatalogoAtivoPorNomeLink(String nomeLink) {
        return repository.findByNomeLink(nomeLink)
                .filter(ConfigConta::catalogoOnlineEstaAtivo)
                .orElseThrow(() -> new ResourceNotFoundException("Catálogo não encontrado"));
    }

    private NomeLinkResponse salvarNomeLink(ConfigConta config, String nomeLink) {

        if (nomeLink.equals(config.getNomeLink())) {
            return new NomeLinkResponse(nomeLink);
        }

        if (repository.existsByNomeLink(nomeLink)) {
            throw new BusinessException("Esse nome de link já está em uso");
        }

        config.alterarNomeLink(nomeLink);
        return new NomeLinkResponse(repository.save(config).getNomeLink());
    }

    private String enviarImagem(MultipartFile imagem, String pasta, int tamanhoMaximo, String descricao) {

        if (imagem == null || imagem.isEmpty()) {
            throw new BusinessException("Imagem " + descricao + " é obrigatória");
        }

        try {
            return storageService.upload(imagem, pasta, tamanhoMaximo);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("Erro ao enviar imagem " + descricao + ": " + e.getMessage());
        }
    }

    // "Farmácia São João" -> "farmacia-sao-joao"; se já existir, "farmacia-sao-joao-2", "-3"...
    private String gerarNomeLinkDisponivel(String nomeEmpresa) {

        String base = Normalizer.normalize(nomeEmpresa == null ? "" : nomeEmpresa, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");

        if (base.length() < NOME_LINK_MIN) {
            base = base.isEmpty() ? "empresa" : "empresa-" + base;
        }

        // Reserva espaço para o sufixo numérico
        base = cortar(base, NOME_LINK_MAX - 6);

        String candidato = base;
        int sufixo = 2;

        while (repository.existsByNomeLink(candidato)) {
            candidato = base + "-" + sufixo++;
        }

        return candidato;
    }

    private String cortar(String valor, int tamanho) {
        if (valor.length() <= tamanho) {
            return valor;
        }
        return valor.substring(0, tamanho).replaceAll("-+$", "");
    }

    private ConfigConta buscarOuCriarDaEmpresaAtual() {
        return repository.findByEmpresa_Id(empresaContext.getEmpresaId())
                .orElseGet(() -> repository.save(new ConfigConta(empresaContext.getEmpresa())));
    }
}
