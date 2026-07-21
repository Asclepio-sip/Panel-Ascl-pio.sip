package Asclepio.Empresa;

import Asclepio.config.security.UsuarioAutenticado;
import Asclepio.Usuario.User.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EmpresaContext {


    public UsuarioAutenticado getUsuarioAutenticado() {

        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();


        if (auth == null || !(auth.getPrincipal() instanceof UsuarioAutenticado usuario)) {

            throw new IllegalStateException("Usuário não autenticado");
        }


        return usuario;
    }


    public User getUsuario() {

        return getUsuarioAutenticado().getUser();
    }


    public Long getEmpresaId() {

        Empresa empresa = getEmpresa();


        return empresa.getId();
    }


    public Empresa getEmpresa() {

        Empresa empresa = getUsuario().getEmpresa();


        if (empresa == null) {

            throw new IllegalStateException(
                    "Usuário sem empresa selecionada"
            );
        }


        return empresa;
    }
}