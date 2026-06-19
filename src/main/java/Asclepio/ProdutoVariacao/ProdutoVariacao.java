package Asclepio.ProdutoVariacao;

import Asclepio.Produto.Product;
import jakarta.persistence.*;

public class ProdutoVariacao {

    private Long id;

    private String nomeVariacao;

    private String codigoBarras;

    private Boolean ativo = true;

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