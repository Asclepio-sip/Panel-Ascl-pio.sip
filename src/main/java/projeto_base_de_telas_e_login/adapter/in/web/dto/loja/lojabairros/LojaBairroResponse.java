package projeto_base_de_telas_e_login.adapter.in.web.dto.loja.lojabairros;

import java.math.BigDecimal;

public class LojaBairroResponse {

    private Long lojaId;
    private Long bairroId;
    private BigDecimal valorFrete;

    public LojaBairroResponse(Long lojaId, Long bairroId, BigDecimal valorFrete) {
        this.lojaId = lojaId;
        this.bairroId = bairroId;
        this.valorFrete = valorFrete;
    }

    public static LojaBairroResponse fromDomain(projeto_base_de_telas_e_login.domain.model.Loja.LojaBairro lb) {
        return new LojaBairroResponse(
                lb.getLoja().getId(),
                lb.getBairro().getId(),
                lb.getValorFrete()
        );
    }

    public Long getLojaId() { return lojaId; }
    public Long getBairroId() { return bairroId; }
    public BigDecimal getValorFrete() { return valorFrete; }
}