package Asclepio.ProdutoVariacao;

import Asclepio.Produto.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_PRODUTO_VARIACAO",

        uniqueConstraints = {

                @UniqueConstraint(name = "UK_PRODUTO_VARIACAO_NOME", columnNames = {"PROV_PRODUTO_ID", "PROV_NOME"}),

                @UniqueConstraint(name = "UK_PRODUTO_VARIACAO_CODIGO_BARRAS", columnNames = "PROV_CODIGO_BARRAS")},

        indexes = {

                @Index(name = "IDX_PRODUTO_VARIACAO_PRODUTO", columnList = "PROV_PRODUTO_ID")})
public class ProdutoVariacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROV_ID")
    private Long id;

    @Column(name = "PROV_NOME", nullable = false, length = 150)
    private String nomeVariacao;

    @Column(name = "PROV_CODIGO_BARRAS", length = 100, unique = true)
    private String codigoBarras;

    @Column(name = "PROV_ATIVO", nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROV_PRODUTO_ID", nullable = false)
    private Product produto;

    public ProdutoVariacao() {
    }

    public ProdutoVariacao(Long id, String nomeVariacao, String codigoBarras, Boolean ativo, Product produto) {

        alterarNome(nomeVariacao);

        this.id = id;

        this.codigoBarras = codigoBarras;

        this.ativo = ativo != null ? ativo : true;

        this.produto = produto;
    }

    public ProdutoVariacao(String nomeVariacao, String codigoBarras, Product produto) {

        this(null, nomeVariacao, codigoBarras, true, produto);
    }

    public void alterarNome(String nomeVariacao) {

        validarNome(nomeVariacao);

        this.nomeVariacao = nomeVariacao.trim();
    }

    public void alterarCodigoBarras(String codigoBarras) {

        this.codigoBarras = codigoBarras;
    }

    public void ativar() {

        this.ativo = true;
    }

    public void desativar() {

        this.ativo = false;
    }

    private void validarNome(String nomeVariacao) {

        if (nomeVariacao == null || nomeVariacao.isBlank()) {

            throw new IllegalArgumentException("Nome da variação é obrigatório");
        }

        if (nomeVariacao.length() > 150) {

            throw new IllegalArgumentException("Nome da variação deve ter no máximo 150 caracteres");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNomeVariacao() {
        return nomeVariacao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Product getProduto() {
        return produto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeVariacao(String nomeVariacao) {

        alterarNome(nomeVariacao);
    }

    public void setCodigoBarras(String codigoBarras) {

        this.codigoBarras = codigoBarras;
    }

    public void setAtivo(Boolean ativo) {

        this.ativo = ativo;
    }

    public void setProduto(Product produto) {

        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof ProdutoVariacao other)) return false;

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}