package Asclepio.Empresa;

import Asclepio.ClienteEmpresa.ClienteEmpresa;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_EMPRESA",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_EMP_CNPJ",
                        columnNames = "EMP_CNPJ")
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

    @OneToMany(mappedBy = "empresa")
    private List<ClienteEmpresa> clientes;

    public Empresa() {
    }

    public Empresa(Long id, String nome, String cnpj, Boolean ativa, LocalDateTime criadoEm, List<ClienteEmpresa> clientes) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.ativa = ativa;
        this.criadoEm = criadoEm;
        this.clientes = clientes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<ClienteEmpresa> getClientes() {
        return clientes;
    }

    public void setClientes(List<ClienteEmpresa> clientes) {
        this.clientes = clientes;
    }
}