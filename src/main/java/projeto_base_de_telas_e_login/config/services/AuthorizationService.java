package projeto_base_de_telas_e_login.config.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.domain.repository.UserPorta;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserPorta userPorta;

    public AuthorizationService(UserPorta userPorta) {
        this.userPorta = userPorta;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userPorta.findByLogin(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado")
                );
    }
}
