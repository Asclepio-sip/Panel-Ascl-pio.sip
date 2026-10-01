package Asclepio.CatalogoOnline;

import Asclepio.CatalogoOnline.dto.CatalogoOnlineFiltro;
import Asclepio.CatalogoOnline.dto.CatalogoPedidoRequest;
import Asclepio.CatalogoOnline.dto.CatalogoPedidoResponse;
import Asclepio.CatalogoOnline.dto.CatalogoCategoriaResponse;
import Asclepio.CatalogoOnline.dto.CatalogoCheckoutResponse;
import Asclepio.CatalogoOnline.dto.CatalogoLojaResponse;
import Asclepio.CatalogoOnline.dto.CatalogoProdutoResponse;
import Asclepio.CatalogoOnline.dto.CatalogoResponse;
import Asclepio.Categoria.CategoriaRepository;
import Asclepio.ConfigConta.ConfigConta;
import Asclepio.ConfigConta.ConfigContaService;
import Asclepio.ConfigConta.dto.DesignCatalogoResponse;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.ItemPedido.DTO.ItemPedidoAddDTO;
import Asclepio.Loja.FormaPagamento.LojaFormaPagamentoRepository;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Loja.LojaBairro.Repository.LojaBairroRepository;
import Asclepio.Pedido.Enum.TipoAtendimentoPedido;
import Asclepio.Pedido.PedidoService;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Rota pública: não usa EmpresaContext (não há token), o tenant vem da loja.
@Service
public class CatalogoOnlineService {

    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoVariacaoService produtoVariacaoService;
    private final ConfigContaService configContaService;
    private final PedidoService pedidoService;
    private final CategoriaRepository categoriaRepository;
    private final LojaFormaPagamentoRepository lojaFormaPagamentoRepository;
    private final LojaBairroRepository lojaBairroRepository;

    public CatalogoOnlineService(
            LojaRepository lojaRepository,
            EstoqueRepository estoqueRepository,
            ProdutoVariacaoService produtoVariacaoService,
            ConfigContaService configContaService,
            PedidoService pedidoService,
            CategoriaRepository categoriaRepository,
            LojaFormaPagamentoRepository lojaFormaPagamentoRepository,
            LojaBairroRepository lojaBairroRepository
    ) {
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
        this.produtoVariacaoService = produtoVariacaoService;
        this.configContaService = configContaService;
        this.pedidoService = pedidoService;
        this.categoriaRepository = categoriaRepository;
        this.lojaFormaPagamentoRepository = lojaFormaPagamentoRepository;
        this.lojaBairroRepository = lojaBairroRepository;
    }

    @Transactional(readOnly = true)
    public CatalogoResponse buscarCatalogo(String nomeLink) {

        ConfigConta config = configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink);
        Long empresaId = config.getEmpresa().getId();

        return new CatalogoResponse(
                config.getNomeLink(),
                config.getEmpresa().getNome(),
                DesignCatalogoResponse.fromEntity(config),
                lojasDaEmpresa(empresaId),
                categoriasDaEmpresa(empresaId)
        );
    }

    @Transactional(readOnly = true)
    public List<CatalogoCategoriaResponse> listarCategorias(String nomeLink) {
        ConfigConta config = configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink);
        return categoriasDaEmpresa(config.getEmpresa().getId());
    }

    @Transactional(readOnly = true)
    public DesignCatalogoResponse buscarDesign(String nomeLink) {
        return DesignCatalogoResponse.fromEntity(configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink));
    }

    @Transactional(readOnly = true)
    public List<CatalogoLojaResponse> listarLojas(String nomeLink) {

        ConfigConta config = configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink);
        return lojasDaEmpresa(config.getEmpresa().getId());
    }

    private List<CatalogoLojaResponse> lojasDaEmpresa(Long empresaId) {
        return lojaRepository.findByEmpresa_IdOrderByNomeLojaAsc(empresaId)
                .stream()
                .map(CatalogoLojaResponse::fromEntity)
                .toList();
    }

    // Só categorias ativas
    private List<CatalogoCategoriaResponse> categoriasDaEmpresa(Long empresaId) {
        return categoriaRepository.findByEmpresaIdAndAtivaTrueOrderByNomeCategoriaAsc(empresaId)
                .stream()
                .map(CatalogoCategoriaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<CatalogoProdutoResponse> listarProdutos(String nomeLink, Long lojaId, CatalogoOnlineFiltro filtro, Pageable pageable) {

        // Nome inexistente, catálogo desligado ou loja de outra empresa: mesma resposta, para não expor nada
        ConfigConta config = configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink);

        validarLojaDoCatalogo(config, lojaId);

        return estoqueRepository
                .findAll(CatalogoOnlineSpecification.filtrar(lojaId, filtro), pageable)
                .map(estoque -> CatalogoProdutoResponse.fromDomain(
                        estoque,
                        produtoVariacaoService.buscarPorIdDTO(estoque.getVariacaoId())
                ));
    }

    // Pedido feito pelo cliente no catálogo público: valida o catálogo e delega para o fluxo
    // normal de pedido online (PedidoService), que já faz a baixa de estoque e o rastreio.
    @Transactional
    public CatalogoPedidoResponse criarPedido(String nomeLink, Long lojaId, CatalogoPedidoRequest dto) {

        ConfigConta config = configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink);

        validarLojaDoCatalogo(config, lojaId);

        if (dto == null || dto.itens() == null || dto.itens().isEmpty()) {
            throw new BusinessException("Pedido precisa ter itens");
        }

        if (dto.tipoAtendimentoPedido() == TipoAtendimentoPedido.BALCAO) {
            throw new BusinessException("Pedido de balcão não pode ser feito pelo catálogo");
        }

        validarItensDoCatalogo(dto.itens());

        return CatalogoPedidoResponse.fromPedidoCriado(
                pedidoService.criarPedido(dto.toPedidoAddDTO(lojaId))
        );
    }

    // Opções de finalização do pedido: atendimento, formas de pagamento ativas e bairros com frete
    @Transactional(readOnly = true)
    public CatalogoCheckoutResponse buscarCheckout(String nomeLink, Long lojaId) {

        ConfigConta config = configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink);
        Loja loja = validarLojaDoCatalogo(config, lojaId);

        // Mesmas regras do PedidoAtendimentoService (entrega terceirizada ainda não existe)
        List<TipoAtendimentoPedido> tiposAtendimento = new ArrayList<>();

        if (loja.aceitaRetirada()) {
            tiposAtendimento.add(TipoAtendimentoPedido.RETIRADA_NA_LOJA);
        }

        if (loja.aceitaEntrega()) {
            tiposAtendimento.add(TipoAtendimentoPedido.ENTREGA_PROPRIA);
        }

        List<CatalogoCheckoutResponse.Bairro> bairros = loja.aceitaEntrega()
                ? lojaBairroRepository.findByLoja_IdOrderByBairro_NomeAsc(lojaId)
                        .stream()
                        .map(CatalogoCheckoutResponse.Bairro::fromEntity)
                        .toList()
                : List.of();

        return new CatalogoCheckoutResponse(
                loja.getId(),
                loja.getNomeLoja(),
                tiposAtendimento,
                formasPagamentoAtivas(lojaId),
                bairros,
                loja.getValorMinimoFreteGratis()
        );
    }

    @Transactional(readOnly = true)
    public List<CatalogoCheckoutResponse.FormaPagamento> listarFormasPagamento(String nomeLink, Long lojaId) {
        ConfigConta config = configContaService.buscarCatalogoAtivoPorNomeLink(nomeLink);
        validarLojaDoCatalogo(config, lojaId);
        return formasPagamentoAtivas(lojaId);
    }

    // Só as formas ativas: são as únicas que o PedidoService aceita
    private List<CatalogoCheckoutResponse.FormaPagamento> formasPagamentoAtivas(Long lojaId) {
        return lojaFormaPagamentoRepository
                .findAllByLoja_IdOrderByFormaPagamento(lojaId)
                .stream()
                .filter(forma -> Boolean.TRUE.equals(forma.getAtivo()))
                .map(CatalogoCheckoutResponse.FormaPagamento::fromEntity)
                .toList();
    }

    private Loja validarLojaDoCatalogo(ConfigConta config, Long lojaId) {
        return lojaRepository.findByIdAndEmpresa_Id(lojaId, config.getEmpresa().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Catálogo não encontrado"));
    }

    // Só deixa pedir o que o catálogo mostra: variação ativa e sem item repetido
    private void validarItensDoCatalogo(List<ItemPedidoAddDTO> itens) {

        Set<Long> variacoes = new HashSet<>();

        for (ItemPedidoAddDTO item : itens) {

            if (item == null || item.variacaoId() == null) {
                throw new BusinessException("Variação obrigatória");
            }

            if (!variacoes.add(item.variacaoId())) {
                throw new BusinessException("A mesma variação foi enviada mais de uma vez; some as quantidades em um único item");
            }

            if (!Boolean.TRUE.equals(produtoVariacaoService.buscarPorIdDTO(item.variacaoId()).ativo())) {
                throw new BusinessException("Produto indisponível no catálogo (variação id: " + item.variacaoId() + ")");
            }
        }
    }
}
