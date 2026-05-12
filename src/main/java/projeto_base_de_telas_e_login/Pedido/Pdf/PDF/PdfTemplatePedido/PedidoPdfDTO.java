//package projeto_base_de_telas_e_login.persistence.Pedido.PDF.PdfTemplatePedido;
//
//import projeto_base_de_telas_e_login.model.Pedido.Enum.FormaDePagamento;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class PedidoPdfDTO {
//
//    // =========================
//    // IDENTIFICAÇÃO DO PEDIDO
//    // =========================
//    private Long id;
//    private String numeroPedidoExterno; // Ex: PED-2026-0001
//    private LocalDateTime criadoEm;
//    private LocalDateTime pagoEm;
//
//    private String statusPedido;
//    private String statusPagamento;
//
//    // =========================
//    // ENTREGA
//    // =========================
//    private String tipoEntrega; // ENTREGA / RETIRADA
//    private String codigoRastreio;
//    private LocalDateTime previsaoEntrega;
//
//    // =========================
//    // PAGAMENTO
//    // =========================
//    private FormaDePagamento formaDePagamento;
//    private String idTransacao; // PIX / Cartão
//    private BigDecimal valorTroco; // Se pagamento for dinheiro
//
//    // =========================
//    // CLIENTE
//    // =========================
//    private Long clienteId;
//    private String nomeCliente;
//    private String email;
//    private String telefone;
//
//    // =========================
//    // ENDEREÇO
//    // =========================
//    private String endereco;
//    private String numero;
//    private String bairro;
//    private String cidade;
//    private String cep;
//    private String complemento;
//
//    // =========================
//    // OBSERVAÇÃO
//    // =========================
//    private String observacao;
//
//    // =========================
//    // VALORES
//    // =========================
//    private BigDecimal totalProdutos;
//    private BigDecimal valorFrete;
//    private BigDecimal valorDesconto;
//    private String cupomDesconto;
//    private BigDecimal outrasTaxas;
//    private BigDecimal totalFinal;
//    private Boolean freteGratis;
//
//    // =========================
//    // ITENS
//    // =========================
//    private List<ItemPedidoPdfDTO> itens;
//
//    public PedidoPdfDTO(
//            Long id,
//            String numeroPedidoExterno,
//            LocalDateTime criadoEm,
//            LocalDateTime pagoEm,
//            String statusPedido,
//            String statusPagamento,
//            String tipoEntrega,
//            String codigoRastreio,
//            LocalDateTime previsaoEntrega,
//            FormaDePagamento formaDePagamento,
//            String idTransacao,
//            BigDecimal valorTroco,
//            Long clienteId,
//            String nomeCliente,
//            String email,
//            String telefone,
//            String endereco,
//            String numero,
//            String bairro,
//            String cidade,
//            String cep,
//            String complemento,
//            String observacao,
//            BigDecimal totalProdutos,
//            BigDecimal valorFrete,
//            BigDecimal valorDesconto,
//            String cupomDesconto,
//            BigDecimal outrasTaxas,
//            BigDecimal totalFinal,
//            Boolean freteGratis,
//            List<ItemPedidoPdfDTO> itens
//    ) {
//        this.id = id;
//        this.numeroPedidoExterno = numeroPedidoExterno;
//        this.criadoEm = criadoEm;
//        this.pagoEm = pagoEm;
//        this.statusPedido = statusPedido;
//        this.statusPagamento = statusPagamento;
//        this.tipoEntrega = tipoEntrega;
//        this.codigoRastreio = codigoRastreio;
//        this.previsaoEntrega = previsaoEntrega;
//        this.formaDePagamento = formaDePagamento;
//        this.idTransacao = idTransacao;
//        this.valorTroco = valorTroco;
//        this.clienteId = clienteId;
//        this.nomeCliente = nomeCliente;
//        this.email = email;
//        this.telefone = telefone;
//        this.endereco = endereco;
//        this.numero = numero;
//        this.bairro = bairro;
//        this.cidade = cidade;
//        this.cep = cep;
//        this.complemento = complemento;
//        this.observacao = observacao;
//        this.totalProdutos = totalProdutos;
//        this.valorFrete = valorFrete;
//        this.valorDesconto = valorDesconto;
//        this.cupomDesconto = cupomDesconto;
//        this.outrasTaxas = outrasTaxas;
//        this.totalFinal = totalFinal;
//        this.freteGratis = freteGratis;
//        this.itens = itens;
//    }
//
//    // GETTERS
//
//    public Long getId() { return id; }
//    public String getNumeroPedidoExterno() { return numeroPedidoExterno; }
//    public LocalDateTime getCriadoEm() { return criadoEm; }
//    public LocalDateTime getPagoEm() { return pagoEm; }
//    public String getStatusPedido() { return statusPedido; }
//    public String getStatusPagamento() { return statusPagamento; }
//    public String getTipoEntrega() { return tipoEntrega; }
//    public String getCodigoRastreio() { return codigoRastreio; }
//    public LocalDateTime getPrevisaoEntrega() { return previsaoEntrega; }
//    public FormaDePagamento getFormaDePagamento() { return formaDePagamento; }
//    public String getIdTransacao() { return idTransacao; }
//    public BigDecimal getValorTroco() { return valorTroco; }
//    public Long getClienteId() { return clienteId; }
//    public String getNomeCliente() { return nomeCliente; }
//    public String getEmail() { return email; }
//    public String getTelefone() { return telefone; }
//    public String getEndereco() { return endereco; }
//    public String getNumero() { return numero; }
//    public String getBairro() { return bairro; }
//    public String getCidade() { return cidade; }
//    public String getCep() { return cep; }
//    public String getComplemento() { return complemento; }
//    public String getObservacao() { return observacao; }
//    public BigDecimal getTotalProdutos() { return totalProdutos; }
//    public BigDecimal getValorFrete() { return valorFrete; }
//    public BigDecimal getValorDesconto() { return valorDesconto; }
//    public String getCupomDesconto() { return cupomDesconto; }
//    public BigDecimal getOutrasTaxas() { return outrasTaxas; }
//    public BigDecimal getTotalFinal() { return totalFinal; }
//    public Boolean getFreteGratis() { return freteGratis; }
//    public List<ItemPedidoPdfDTO> getItens() { return itens; }
//}