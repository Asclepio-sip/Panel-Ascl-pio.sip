package Asclepio.Empresa;

import Asclepio.Usuario.User.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EmpresaContext {

    public User getUsuario() {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        return user;
    }

    public Long getEmpresaId() {

        Empresa empresa = getUsuario().getEmpresa();

        if (empresa == null) {
            throw new IllegalStateException("Usuário sem empresa vinculada");
        }

        return empresa.getId();
    }

    public Empresa getEmpresa() {

        Empresa empresa = getUsuario().getEmpresa();

        if (empresa == null) {
            throw new IllegalStateException("Usuário sem empresa vinculada");
        }

        return empresa;
    }
}