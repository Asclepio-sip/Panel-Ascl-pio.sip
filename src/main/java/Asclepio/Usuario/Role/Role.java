package Asclepio.Usuario.Role;

import Asclepio.Usuario.Permission.Permission;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_ROLE", uniqueConstraints = {@UniqueConstraint(name = "UK_ROLE_NOME", columnNames = "ROL_NOME")})
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ROL_ID", nullable = false)
    private UUID id;

    @Column(name = "ROL_NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "ROL_DESCRICAO", length = 255)
    private String descricao;

    @ManyToMany(fetch = FetchType.EAGER)

    @JoinTable(

            name = "TB_ROLE_PERMISSION",

            joinColumns = @JoinColumn(name = "ROP_ROLE_ID", referencedColumnName = "ROL_ID"),

            inverseJoinColumns = @JoinColumn(name = "ROP_PERMISSION_ID", referencedColumnName = "PER_ID"))
    private List<Permission> permissions = new ArrayList<>();

    public Role() {
    }

    public Role(UUID id, String nome, String descricao, List<Permission> permissions) {

        alterarNome(nome);

        this.id = id;

        this.descricao = descricao;

        this.permissions = permissions != null ? permissions : new ArrayList<>();
    }

// =====================
// REGRAS DE NEGÓCIO
// =====================

    public void alterarNome(String nome) {

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException("Nome da role é obrigatório");
        }

        this.nome = nome.trim();
    }

    public void alterarDescricao(String descricao) {

        this.descricao = descricao;
    }

    public void adicionarPermissao(Permission permission) {

        if (permission == null) {

            throw new IllegalArgumentException("Permissão inválida");
        }

        if (!permissions.contains(permission)) {

            permissions.add(permission);
        }
    }

    public void removerPermissao(Permission permission) {

        permissions.remove(permission);
    }

    public boolean possuiPermissao(String nomePermissao) {

        return permissions.stream()

                .anyMatch(p ->

                        p.getNome()

                                .equalsIgnoreCase(nomePermissao));
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

    public List<Permission> getPermissions() {
        return permissions;
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

        this.descricao = descricao;
    }

    public void setPermissions(List<Permission> permissions) {

        this.permissions = permissions;
    }

// =====================
// EQUALS E HASHCODE
// =====================

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Role other)) return false;

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
