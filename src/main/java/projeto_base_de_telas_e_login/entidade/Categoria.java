package projeto_base_de_telas_e_login.entidade;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_categoria", nullable = false, unique = true)
    private String nomeCategoria;

    public Categoria() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}