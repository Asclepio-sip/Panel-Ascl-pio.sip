package Asclepio.Usuario.Role;

import Asclepio.Empresa.Empresa;
import Asclepio.Usuario.Permission.Permission;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "TB_ROLE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_ROLE_NOME_EMPRESA",
                        columnNames = {"ROL_NOME", "ROL_EMPRESA_ID"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ROL_ID", nullable = false)
    private UUID id;


    @Column(name = "ROL_NOME", nullable = false, length = 100)
    private String nome;


    @Column(name = "ROL_DESCRICAO", length = 255)
    private String descricao;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROL_EMPRESA_ID", nullable = false)
    private Empresa empresa;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TB_ROLE_PERMISSION",
            joinColumns = @JoinColumn(name = "ROP_ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROP_PERMISSION_ID")
    )
    @Builder.Default
    private List<Permission> permissions = new ArrayList<>();


    // =====================
    // REGRAS DE NEGÓCIO
    // =====================

    public void alterarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da role é obrigatório");
        }

        this.nome = nome.trim();
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
                        p.getNome().equalsIgnoreCase(nomePermissao)
                );
    }


    // =====================
    // MÉTODOS DE NEGÓCIO
    // =====================

    public void atualizarNome(String nome) {
        alterarNome(nome);
    }


    public void adicionarEmpresa(Empresa empresa) {

        if (empresa == null) {
            throw new IllegalArgumentException("Empresa obrigatória");
        }

        this.empresa = empresa;
    }


    // =====================
    // EQUALS E HASHCODE
    // =====================

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Role role)) return false;

        return id != null && id.equals(role.id);
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}