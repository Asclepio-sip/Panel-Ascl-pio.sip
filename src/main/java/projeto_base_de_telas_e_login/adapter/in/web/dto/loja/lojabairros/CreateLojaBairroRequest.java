package projeto_base_de_telas_e_login.adapter.in.web.dto.loja.lojabairros;

import java.math.BigDecimal;

public class CreateLojaBairroRequest {
    private Long lojaId;
    private String lojaNome;
    private Long bairroId;
    private String bairroNome;
    private BigDecimal valorFrete;

    // Getters e Setters
    public Long getLojaId() { return lojaId; }
    public void setLojaId(Long lojaId) { this.lojaId = lojaId; }

    public String getLojaNome() { return lojaNome; }
    public void setLojaNome(String lojaNome) { this.lojaNome = lojaNome; }

    public Long getBairroId() { return bairroId; }
    public void setBairroId(Long bairroId) { this.bairroId = bairroId; }

    public String getBairroNome() { return bairroNome; }
    public void setBairroNome(String bairroNome) { this.bairroNome = bairroNome; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }
}