package Asclepio.Usuario.Permission;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "TB_PERMISSION",

        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_PERMISSION_NOME",
                        columnNames = "PER_NOME"
                )
        }
)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PER_ID", nullable = false)
    private UUID id;

    @Column(
            name = "PER_NOME",
            nullable = false,
            length = 100
    )
    private String nome;

    @Column(
            name = "PER_DESCRICAO",
            nullable = false,
            length = 255
    )
    private String descricao;

    public Permission() {
    }

    public Permission(
            UUID id,
            String nome,
            String descricao
    ) {

        alterarNome(nome);

        alterarDescricao(descricao);

        this.id = id;
    }

    // =====================
    // REGRAS DE NEGÓCIO
    // =====================

    public void alterarNome(String nome) {

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException(
                    "Nome da permissão é obrigatório"
            );
        }

        this.nome = nome.trim();
    }

    public void alterarDescricao(String descricao) {

        if (descricao == null || descricao.isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição da permissão é obrigatória"
            );
        }

        this.descricao = descricao.trim();
    }

    // =====================
    // GETTERS
    // =====================

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    // =====================
    // SETTERS
    // =====================

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNome(String nome) {
        alterarNome(nome);
    }

    public void setDescricao(String descricao) {
        alterarDescricao(descricao);
    }

    // =====================
    // EQUALS E HASHCODE
    // =====================

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Permission other))
            return false;

        return id != null
                && id.equals(other.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}