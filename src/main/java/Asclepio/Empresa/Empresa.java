package Asclepio.Empresa;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_EMPRESA", uniqueConstraints = {
        @UniqueConstraint(name = "UK_EMP_CNPJ", columnNames = "EMP_CNPJ")
})
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMP_ID")
    private Long id;

    @Column(name = "EMP_NOME", nullable = false, length = 150)
    private String nome;

    @Column(name = "EMP_CNPJ", length = 18)
    private String cnpj;

    @Column(name = "EMP_ATIVA", nullable = false)
    private Boolean ativa = true;

    @Column(name = "EMP_CRIADO_EM", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();



    public Empresa() {
    }

    public Empresa(Long id, String nome, String cnpj) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.ativa = true;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}