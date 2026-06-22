package Asclepio.Usuario.User;

import Asclepio.Empresa.Empresa;
import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.Role.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "TB_USER", uniqueConstraints = {@UniqueConstraint(name = "UK_USER_EMPRESA_USERNAME", columnNames = {"USR_EMPRESA_ID", "USR_USERNAME"}), @UniqueConstraint(name = "UK_USER_EMPRESA_EMAIL", columnNames = {"USR_EMPRESA_ID", "USR_EMAIL"})})
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USR_ID")
    private UUID id;

    @Column(name = "USR_USERNAME", nullable = false, length = 100)
    private String username;

    @Column(name = "USR_PASSWORD", nullable = false)
    private String password;

    @Column(name = "USR_EMAIL", nullable = false, length = 150)
    private String email;

    @Column(name = "USR_ATIVO", nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(name = "USR_ROLE_ID", nullable = false)
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)

    @JoinTable(

            name = "TB_USER_PERMISSION",

            joinColumns = @JoinColumn(name = "USP_USER_ID", referencedColumnName = "USR_ID"),

            inverseJoinColumns = @JoinColumn(name = "USP_PERMISSION_ID", referencedColumnName = "PER_ID"))
    private List<Permission> permissionsExtras = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USR_EMPRESA_ID", nullable = false)
    private Empresa empresa;

    public User() {
    }

// ==========================
// USER DETAILS
// ==========================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<String> permissoes = new HashSet<>();

        if (role != null) {

            role.getPermissions()

                    .forEach(p ->

                            permissoes.add(p.getNome()));
        }

        permissionsExtras

                .forEach(p ->

                        permissoes.add(p.getNome()));

        return permissoes

                .stream()

                .map(SimpleGrantedAuthority::new)

                .collect(Collectors.toList());
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return ativo;
    }

// ==========================
// REGRAS DE NEGÓCIO
// ==========================

    public void ativar() {

        this.ativo = true;
    }

    public void desativar() {

        this.ativo = false;
    }

    public void adicionarPermissaoExtra(Permission permission) {

        if (permission != null && !permissionsExtras.contains(permission)) {

            permissionsExtras.add(permission);
        }
    }

    public void removerPermissaoExtra(Permission permission) {

        permissionsExtras.remove(permission);
    }

// ==========================
// GETTERS
// ==========================

    public UUID getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Role getRole() {
        return role;
    }

    public List<Permission> getPermissionsExtras() {
        return permissionsExtras;
    }

// ==========================
// SETTERS
// ==========================

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {

        this.username = username != null ? username.trim() : null;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setEmail(String email) {

        this.email = email != null ? email.trim() : null;
    }

    public void setAtivo(Boolean ativo) {

        this.ativo = ativo;
    }

    public void setRole(Role role) {

        this.role = role;
    }

    public void setPermissionsExtras(List<Permission> permissionsExtras) {

        this.permissionsExtras = permissionsExtras;
    }

// ==========================
// EQUALS E HASHCODE
// ==========================

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof User other)) return false;

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }


}
