package projeto_base_de_telas_e_login.adapter.in.web.dto.loja.Bairro;

import projeto_base_de_telas_e_login.domain.model.Loja.Bairro;

public class BairroResponseDTO {

    private Long id;
    private String nome;

    public BairroResponseDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Factory method pra converter do domain
    public static BairroResponseDTO fromDomain(Bairro bairro) {
        return new BairroResponseDTO(bairro.getId(), bairro.getNome());
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}