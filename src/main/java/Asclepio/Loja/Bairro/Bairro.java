package Asclepio.Loja.Bairro;

import Asclepio.Empresa.Empresa;
import jakarta.persistence.*;

@Entity
@Table(
        name = "TB_BAIRRO",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_BAI_EMPRESA_NOME",
                        columnNames = {"BAI_EMPRESA_ID", "BAI_NOME"}
                )
        }
)
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAI_ID")
    private Long id;

    @Column(name = "BAI_NOME", nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BAI_EMPRESA_ID", nullable = false)
    private Empresa empresa;

    protected Bairro() {
    }

    public Bairro(Long id, String nome, Empresa empresa) {
        this.id = id;
        this.nome = nome;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome != null ? nome.trim() : null;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}