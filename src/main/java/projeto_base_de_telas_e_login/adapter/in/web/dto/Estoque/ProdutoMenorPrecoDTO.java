package projeto_base_de_telas_e_login.adapter.in.web.dto.Estoque;

import java.math.BigDecimal;

public class ProdutoMenorPrecoDTO {

    private Long produtoId;
    private String nomeProduto;
    private BigDecimal precoVenda;
    private String nomeLoja;
    private String imagemBase64;

    public ProdutoMenorPrecoDTO(
            Long produtoId,
            String nomeProduto,
            BigDecimal precoVenda,
            String nomeLoja,
            String imagemBase64
    ) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoVenda = precoVenda;
        this.nomeLoja = nomeLoja;
        this.imagemBase64 = imagemBase64;
    }

    // getters

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public String getImagemBase64() {
        return imagemBase64;
    }

    public void setImagemBase64(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }
}