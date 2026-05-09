package projeto_base_de_telas_e_login.dto.loja.Loja;

import projeto_base_de_telas_e_login.model.Loja.Loja;
import java.math.BigDecimal;

public record LojaResponse(

        Long id,
        String nome,
        String tipoAtendimento,
        BigDecimal valorMinimoFreteGratis) {

    public LojaResponse(Long id, String nome, String tipoAtendimento, BigDecimal valorMinimoFreteGratis) {
        this.id = id;
        this.nome = nome;
        this.tipoAtendimento = tipoAtendimento;
        this.valorMinimoFreteGratis = valorMinimoFreteGratis;
    }

    public static LojaResponse fromDomain(Loja loja) {
        return new LojaResponse(
                loja.getId(),
                loja.getNome(),
                loja.getTipoAtendimento().name(),
                loja.getValorMinimoFreteGratis()
        );
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }

}