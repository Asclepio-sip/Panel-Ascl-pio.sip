

package Asclepio.config.security;

import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.User.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UsuarioAutenticado implements UserDetails {

    private final User user;

    public UsuarioAutenticado(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getEmpresaId() {
        return user.getEmpresa() != null
                ? user.getEmpresa().getId()
                : null;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getAuthorities();
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
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }


}


