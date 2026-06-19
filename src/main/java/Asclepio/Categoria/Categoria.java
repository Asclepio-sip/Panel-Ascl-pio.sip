package Asclepio.Categoria;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

    private Long id;

    private String nomeCategoria;

    private String descricao;

    private String icone;

    private Boolean ativa = true;

    private Categoria categoriaPai;

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