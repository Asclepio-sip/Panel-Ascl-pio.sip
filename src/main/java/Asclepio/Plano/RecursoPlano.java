package Asclepio.Plano;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Linha da tabela de comparação de planos (ex.: "Número de Lojas" / incluído / "Até 3"). */
@Embeddable
public class RecursoPlano {

    @Column(name = "PRE_NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "PRE_INCLUIDO", nullable = false)
    private boolean incluido;

    @Column(name = "PRE_DETALHE", nullable = false, length = 100)
    private String detalhe = "";

    protected RecursoPlano() {
    }

    public RecursoPlano(String nome, boolean incluido, String detalhe) {
        this.nome = nome;
        this.incluido = incluido;
        this.detalhe = detalhe == null ? "" : detalhe;
    }

    public String getNome() {
        return nome;
    }

    public boolean isIncluido() {
        return incluido;
    }

    public String getDetalhe() {
        return detalhe;
    }
}
