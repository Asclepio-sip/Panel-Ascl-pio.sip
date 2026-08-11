package Asclepio.ClienteEmpresa;

import Asclepio.Empresa.Empresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;
@Entity
@Table(name = "TB_CLIENTE_EMPRESA")
public class ClienteEmpresa {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Size(min = 3, max = 100, message = " O nome deve ter entrer 3 e 100 caracteres")
    private String nome;

    @NotNull
    @Pattern(regexp = "^[0-9()+\\-\\s]{8,20}$", message = "Número de telefone inválido")
    private String numero;

    @Email
    private String email;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    public ClienteEmpresa() {
    }
    public ClienteEmpresa(UUID id, String nome, String numero, String email) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.email = email;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
