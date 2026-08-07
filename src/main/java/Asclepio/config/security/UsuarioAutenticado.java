package Asclepio.config.security;

import Asclepio.Usuario.User.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UsuarioAutenticado implements UserDetails {

    private final User user;
    private final Long empresaId;
    private final Long lojaId;
    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioAutenticado(
            User user,
            Long empresaId,
            Long lojaId,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.user = user;
        this.empresaId = empresaId;
        this.lojaId = lojaId;
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public Long getLojaId() {
        return lojaId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getAtivo();
    }
}