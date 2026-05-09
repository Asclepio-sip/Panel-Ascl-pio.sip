package projeto_base_de_telas_e_login.adapter.out.persistence.Loja.Bairro;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.domain.model.Loja.Bairro;

@Entity
@Table(name = "bairro")
public class BairroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    protected BairroEntity() {}

    public BairroEntity(Bairro bairro) {
        this.id = bairro.getId();
        this.nome = bairro.getNome();
    }

    public Bairro toDomain() {
        return new Bairro(id, nome);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}