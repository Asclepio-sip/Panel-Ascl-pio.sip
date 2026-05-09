package projeto_base_de_telas_e_login.adapter.in.web.dto.loja.Loja;

import projeto_base_de_telas_e_login.domain.model.Loja.Loja;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class LojaResponse {

    private Long id;
    private String nome;
    private String tipoAtendimento;
    private BigDecimal valorMinimoFreteGratis;

    public LojaResponse(Long id, String nome, String tipoAtendimento, BigDecimal valorMinimoFreteGratis) {
        this.id = id;
        this.nome = nome;
        this.tipoAtendimento = tipoAtendimento;
        this.valorMinimoFreteGratis = valorMinimoFreteGratis;
    }

    // ✅ Factory method
    public static LojaResponse fromDomain(Loja loja) {
        return new LojaResponse(
                loja.getId(),
                loja.getNome(),
                loja.getTipoAtendimento().name(),
                loja.getValorMinimoFreteGratis()
        );
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getTipoAtendimento() { return tipoAtendimento; }
    public BigDecimal getValorMinimoFreteGratis() { return valorMinimoFreteGratis; }
}