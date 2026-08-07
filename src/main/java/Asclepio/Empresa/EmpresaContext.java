package Asclepio.Empresa;

import Asclepio.config.security.UsuarioAutenticado;
import Asclepio.exception.ResourceNotFoundException;
import com.auth0.jwt.JWT;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class EmpresaContext {

    private final EmpresaRepository empresaRepository;

    public EmpresaContext(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    private UsuarioAutenticado usuario() {

        return (UsuarioAutenticado)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
    }

    public Long getEmpresaId() {
        return usuario().getEmpresaId();
    }

    public Long getLojaId() {
        return usuario().getLojaId();
    }

    public Empresa getEmpresa() {

        return empresaRepository
                .findById(getEmpresaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa não encontrada"));
    }
}