package Asclepio.Categoria;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_CATEGORIA", uniqueConstraints = {
        @UniqueConstraint(name = "UK_CAT_NOME_PAI", columnNames = {
                "CAT_NOME", "CAT_CATEGORIA_PAI_ID"})})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAT_ID")
    private Long id;

    @Column(name = "CAT_NOME", nullable = false, length = 100)
    private String nomeCategoria;

    @Column(name = "CAT_DESCRICAO", length = 500)
    private String descricao;

    @Column(name = "CAT_ICONE", length = 100)
    private String icone;

    @Column(name = "CAT_ATIVA", nullable = false)
    private Boolean ativa = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAT_CATEGORIA_PAI_ID")
    private Categoria categoriaPai;

    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> subcategorias = new ArrayList<>();

    public boolean isCategoriaPrincipal() {
        return categoriaPai == null;
    }

    public boolean possuiSubcategorias() {
        return !subcategorias.isEmpty();
    }

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