package projeto_base_de_telas_e_login.domain.model.categoria;

public class Categoria {

    private Long id;
    private String nomeCategoria;

    public Categoria() {}

    public Categoria(Long id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
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