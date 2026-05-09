package projeto_base_de_telas_e_login.adapter.out.persistence.categoria;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.domain.model.categoria.Categoria;

@Entity
@Table(name = "categoria")
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_categoria", nullable = false, unique = true)
    private String nomeCategoria;

    protected CategoriaEntity() {}

    // DOMAIN → ENTITY
    public CategoriaEntity(Categoria categoria) {
        this.id = categoria.getId();
        this.nomeCategoria = categoria.getNomeCategoria();
    }

    // ENTITY → DOMAIN
    public Categoria toDomain() {
        Categoria categoria = new Categoria();
        categoria.setId(this.id);
        categoria.setNomeCategoria(this.nomeCategoria);
        return categoria;
    }

    public Long getId() { return id; }
    public String getNomeCategoria() { return nomeCategoria; }

    public void setId(Long id) { this.id = id; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }
}