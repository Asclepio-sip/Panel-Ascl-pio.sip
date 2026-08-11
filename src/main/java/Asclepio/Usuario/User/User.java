package Asclepio.Usuario.User;

import Asclepio.Empresa.Empresa;
import Asclepio.UserLoja.UserLoja;
import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.Role.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(
        name = "TB_USER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USER_USERNAME", columnNames = "USR_USERNAME"),
                @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = "USR_EMAIL")
        }
)

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USR_ID")
    private UUID id;

    @Column(name = "USR_USERNAME", nullable = false, length = 100)
    private String username;

    @Column(name = "USR_NOME", length = 150)
    private String nome;

    @Column(name = "USR_PASSWORD", nullable = false)
    private String password;

    @Column(name = "USR_EMAIL", nullable = false, length = 150)
    private String email;

    @Column(name = "USR_ATIVO", nullable = false)
    private Boolean ativo = true;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TB_USER_PERMISSION",
            joinColumns = @JoinColumn(name = "USP_USER_ID", referencedColumnName = "USR_ID"),
            inverseJoinColumns = @JoinColumn(name = "USP_PERMISSION_ID", referencedColumnName = "PER_ID")
    )
    private List<Permission> permissionsExtras = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLoja> userLojas = new ArrayList<>();


    public void setNome(String nome) {
        this.nome = nome != null ? nome.trim() : null;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return permissionsExtras.stream()
                .map(p -> new SimpleGrantedAuthority(p.getNome()))
                .toList();
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
    // SETTERS PERSONALIZADOS
    // ==========================

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public User() {
    }

    public User(UUID id, String username, String nome, String password, String email, Boolean ativo, List<Permission> permissionsExtras, List<UserLoja> userLojas) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.password = password;
        this.email = email;
        this.ativo = ativo;
        this.permissionsExtras = permissionsExtras;
        this.userLojas = userLojas;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Permission> getPermissionsExtras() {
        return permissionsExtras;
    }

    public void setPermissionsExtras(List<Permission> permissionsExtras) {
        this.permissionsExtras = permissionsExtras;
    }

    public List<UserLoja> getUserLojas() {
        return userLojas;
    }

    public void setUserLojas(List<UserLoja> userLojas) {
        this.userLojas = userLojas;
    }
}