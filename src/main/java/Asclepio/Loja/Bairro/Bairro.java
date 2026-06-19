package Asclepio.Loja.Bairro;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_BAIRRO", uniqueConstraints = {@UniqueConstraint(name = "UK_BAI_NOME", columnNames = "BAI_NOME")})
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAI_ID")
    private Long id;

    @Column(name = "BAI_NOME", nullable = false, unique = true, length = 100)
    private String nome;

    protected Bairro() {
    }

    public Bairro(Long id, String nome) {

        this.id = id;
        this.nome = nome;
    }

    public boolean possuiMesmoNome(String nome) {

        return this.nome != null && this.nome.equalsIgnoreCase(nome);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {

        if (nome != null) {
            nome = nome.trim();
        }

        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Bairro bairro)) {
            return false;
        }

        return id != null && id.equals(bairro.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

    @Override
    public String toString() {

        return "Bairro{" + "id=" + id + ", nome='" + nome + '\'' + '}';
    }
}