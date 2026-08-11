package Asclepio.Loja.FormaPagamento;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Pedido.Enum.FormaDePagamento;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "loja_forma_pagamento",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"loja_id", "forma_pagamento"})
       })
public class LojaFormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaDePagamento formaPagamento;

    @Column(nullable = false)
    private Boolean ativo = true;

    public LojaFormaPagamento() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public FormaDePagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaDePagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}