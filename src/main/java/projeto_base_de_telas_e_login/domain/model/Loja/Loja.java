package projeto_base_de_telas_e_login.domain.model.Loja;


import projeto_base_de_telas_e_login.domain.model.Loja.Enum.TipoAtendimentoLoja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Loja {

    private Long id;
    private String nome;
    private TipoAtendimentoLoja tipoAtendimento;
    private List<LojaBairro> bairrosAtendidos = new ArrayList<>();
    private BigDecimal valorMinimoFreteGratis;

    public Loja(Long id, String nome, TipoAtendimentoLoja tipoAtendimento) {
        this.id = id;
        this.nome = nome;
        this.tipoAtendimento = tipoAtendimento;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public TipoAtendimentoLoja getTipoAtendimento() { return tipoAtendimento; }
    public List<LojaBairro> getBairrosAtendidos() { return bairrosAtendidos; }

    public boolean aceitaEntrega() {
        return tipoAtendimento == TipoAtendimentoLoja.ENTREGA
                || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public boolean aceitaRetirada() {
        return tipoAtendimento == TipoAtendimentoLoja.RETIRADA
                || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public void alterarTipoAtendimento(TipoAtendimentoLoja novoTipo) {

        if (novoTipo == TipoAtendimentoLoja.RETIRADA && !bairrosAtendidos.isEmpty()) {
            throw new IllegalStateException(
                    "Não pode virar RETIRADA enquanto houver bairros cadastrados."
            );
        }

        this.tipoAtendimento = novoTipo;
    }

    public void adicionarBairro(Bairro bairro, java.math.BigDecimal valorFrete) {

        if (!aceitaEntrega()) {
            throw new IllegalStateException("Loja não aceita entrega.");
        }

        LojaBairro lojaBairro = new LojaBairro(this, bairro, valorFrete);
        bairrosAtendidos.add(lojaBairro);
    }

    public void removerBairro(Bairro bairro) {
        bairrosAtendidos.removeIf(lb -> lb.getBairro().equals(bairro));
    }

    private BigDecimal buscarFreteDoBairro(Bairro bairro) {

        return bairrosAtendidos.stream()
                .filter(lb -> lb.getBairro().equals(bairro))
                .findFirst()
                .map(LojaBairro::getValorFrete)
                .orElseThrow(() ->
                        new IllegalArgumentException("Bairro não atendido pela loja.")
                );
    }

    public BigDecimal getValorMinimoFreteGratis() {
        return valorMinimoFreteGratis;
    }

    public void configurarFreteGratis(BigDecimal valorMinimo) {
        if (valorMinimo != null && valorMinimo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor mínimo inválido.");
        }
        this.valorMinimoFreteGratis = valorMinimo;
    }
}