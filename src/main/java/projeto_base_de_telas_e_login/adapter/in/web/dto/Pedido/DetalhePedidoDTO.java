package projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido;

import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DetalhePedidoDTO {

    private Long id;
    private String nomeCliente;
    private String email;
    private String telefone;
    private String endereco;
    private String bairro;
    private String complemento;
    private String observacao;
    private String status;
    private String tipoEntrega;
    private LocalDateTime criadoEm;

    private BigDecimal totalProdutos;
    private BigDecimal valorFrete;
    private BigDecimal totalFinal;
    private Boolean freteGratis;

    private List<ItemDTO> itens;

    public DetalhePedidoDTO(
            Long id,
            String nomeCliente,
            String email,
            String telefone,
            String endereco,
            String bairro,
            String complemento,
            String observacao,
            String status,
            String tipoEntrega,
            LocalDateTime criadoEm,
            BigDecimal totalProdutos,
            BigDecimal valorFrete,
            BigDecimal totalFinal,
            Boolean freteGratis,
            List<ItemDTO> itens
    ) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.bairro = bairro;
        this.complemento = complemento;
        this.observacao = observacao;
        this.status = status;
        this.tipoEntrega = tipoEntrega;
        this.criadoEm = criadoEm;
        this.totalProdutos = totalProdutos;
        this.valorFrete = valorFrete;
        this.totalFinal = totalFinal;
        this.freteGratis = freteGratis;
        this.itens = itens;
    }

    public static DetalhePedidoDTO fromDomain(Pedido pedido) {

        List<ItemDTO> itensDTO = pedido.getItens()
                .stream()
                .map(item -> new ItemDTO(
                        item.getNomeProduto(),
                        item.getQuantidade(),
                        item.getPrecoUnitario().getValor(),
                        item.getSubtotal().getValor()
                ))
                .toList();

        return new DetalhePedidoDTO(
                pedido.getId(),
                pedido.getNomeCliente(),
                pedido.getEmail(),
                pedido.getTelefone(),
                pedido.getEndereco(),
                pedido.getBairro(),
                pedido.getComplemento(),
                pedido.getObservacao(),
                pedido.getStatus().name(),
                pedido.getTipoEntrega().name(),
                pedido.getCriadoEm(),
                pedido.getTotalProdutos().getValor(),
                pedido.getValorFrete().getValor(),
                pedido.getTotalFinal().getValor(),
                pedido.getFreteGratis(),
                itensDTO
        );
    }

    public static class ItemDTO {

        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;

        public ItemDTO(String nomeProduto, Integer quantidade,
                       BigDecimal precoUnitario, BigDecimal subtotal) {
            this.nomeProduto = nomeProduto;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.subtotal = subtotal;
        }

        public String getNomeProduto() { return nomeProduto; }
        public Integer getQuantidade() { return quantidade; }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public BigDecimal getSubtotal() { return subtotal; }
    }

    public Long getId() { return id; }
    public String getNomeCliente() { return nomeCliente; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public String getBairro() { return bairro; }
    public String getComplemento() { return complemento; }
    public String getObservacao() { return observacao; }
    public String getStatus() { return status; }
    public String getTipoEntrega() { return tipoEntrega; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public BigDecimal getTotalProdutos() { return totalProdutos; }
    public BigDecimal getValorFrete() { return valorFrete; }
    public BigDecimal getTotalFinal() { return totalFinal; }
    public Boolean getFreteGratis() { return freteGratis; }
    public List<ItemDTO> getItens() { return itens; }
}