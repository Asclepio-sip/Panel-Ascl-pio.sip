package Asclepio.Usuario.User;

import Asclepio.Usuario.User.Repository.UserRepository;
import Asclepio.Usuario.User.dto.RequestCriarContaDTO;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final UserRepository userRepository;

    public UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validarCriacaoConta(RequestCriarContaDTO dto) {

        if (dto == null) {
            throw new BusinessException("Dados obrigatórios.");
        }

        if (dto.nomeEmpresa() == null || dto.nomeEmpresa().isBlank()) {
            throw new BusinessException("Nome da empresa é obrigatório.");
        }

        if (dto.login() == null || dto.login().isBlank()) {
            throw new BusinessException("Login é obrigatório.");
        }

        if (dto.email() == null || dto.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório.");
        }

        if (dto.password() == null || dto.password().isBlank()) {
            throw new BusinessException("Senha é obrigatória.");
        }
    }

    public void validarLogin(String login) {

        if (userRepository.findByUsername(login.trim()).isPresent()) {
            throw new BusinessException("Login já cadastrado.");
        }
    }

    public void validarEmail(String email) {

        if (userRepository.findByEmail(email.trim()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado.");
        }
    }
}