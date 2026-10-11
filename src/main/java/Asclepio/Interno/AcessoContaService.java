package Asclepio.Interno;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaRepository;
import Asclepio.UserLoja.UserLoja;
import Asclepio.UserLoja.UserLojaRepository;
import Asclepio.Usuario.User.Repository.UserRepository;
import Asclepio.Usuario.User.User;
import Asclepio.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Bloqueia / libera o acesso de uma conta (empresa inteira: todos os usuários dela).
 * Quem chama é o painel SaaS, pelo usuário dono da conta (o userId que o POST /interno/contas devolveu).
 */
@Service
public class AcessoContaService {

    private final UserRepository userRepository;
    private final UserLojaRepository userLojaRepository;
    private final EmpresaRepository empresaRepository;

    public AcessoContaService(UserRepository userRepository, UserLojaRepository userLojaRepository,
                              EmpresaRepository empresaRepository) {
        this.userRepository = userRepository;
        this.userLojaRepository = userLojaRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public void alterarAcesso(UUID userId, boolean ativo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário " + userId + " não encontrado."));

        List<Empresa> empresas = userLojaRepository.findAllByUser(user).stream()
                .map(UserLoja::getLoja)
                .map(loja -> loja.getEmpresa())
                .distinct()
                .toList();

        if (empresas.isEmpty()) {
            throw new ResourceNotFoundException("Usuário " + userId + " não tem empresa.");
        }

        for (Empresa empresa : empresas) {
            empresa.setAtiva(ativo);
            empresaRepository.save(empresa);
        }
    }
}
