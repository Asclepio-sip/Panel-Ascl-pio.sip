package projeto_base_de_telas_e_login.Usuario.User;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import projeto_base_de_telas_e_login.Usuario.Permission.Permission;
import projeto_base_de_telas_e_login.Usuario.Role.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String email;

    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissionsExtras = new ArrayList<>();

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        role.getPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getNome()))
        );

        permissionsExtras.forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getNome()))
        );

        return authorities;
    }

    public List<Permission> getPermissionsExtras() {
        return permissionsExtras;
    }

    public void setPermissionsExtras(List<Permission> permissionsExtras) {
        this.permissionsExtras = permissionsExtras;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }


}