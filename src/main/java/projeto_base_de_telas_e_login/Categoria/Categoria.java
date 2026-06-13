package projeto_base_de_telas_e_login.Categoria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_categoria", nullable = false)
    private String nomeCategoria;

    @Column(length = 500)
    private String descricao;

    private String icone;

    private Boolean ativa = true;

    @ManyToOne
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;

    @JsonIgnore
    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> subcategorias = new ArrayList<>();

    public Categoria() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getIcone() {
        return icone;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public Categoria getCategoriaPai() {
        return categoriaPai;
    }

    public List<Categoria> getSubcategorias() {
        return subcategorias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public void setCategoriaPai(Categoria categoriaPai) {
        this.categoriaPai = categoriaPai;
    }

    public void setSubcategorias(List<Categoria> subcategorias) {
        this.subcategorias = subcategorias;
    }
}