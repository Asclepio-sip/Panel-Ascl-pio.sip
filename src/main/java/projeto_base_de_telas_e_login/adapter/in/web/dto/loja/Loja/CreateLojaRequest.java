package projeto_base_de_telas_e_login.adapter.in.web.dto.loja.Loja;
import projeto_base_de_telas_e_login.domain.model.Loja.Enum.TipoAtendimentoLoja;
import java.math.BigDecimal;

public class CreateLojaRequest {

    private String nome;
    private TipoAtendimentoLoja tipoAtendimento;      // ADICIONADO
    private BigDecimal valorMinimoFreteGratis;        // ADICIONADO

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoAtendimentoLoja getTipoAtendimento() { return tipoAtendimento; }
    public void setTipoAtendimento(TipoAtendimentoLoja tipoAtendimento) { this.tipoAtendimento = tipoAtendimento; }

    public BigDecimal getValorMinimoFreteGratis() { return valorMinimoFreteGratis; }
    public void setValorMinimoFreteGratis(BigDecimal valorMinimoFreteGratis) { this.valorMinimoFreteGratis = valorMinimoFreteGratis; }
}