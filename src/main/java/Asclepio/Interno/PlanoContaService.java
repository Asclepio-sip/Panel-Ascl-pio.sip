package Asclepio.Interno;

import Asclepio.Empresa.Empresa;
import Asclepio.Plano.Plano;
import Asclepio.Plano.PlanoRepository;
import Asclepio.UserLoja.UserLoja;
import Asclepio.UserLoja.UserLojaRepository;
import Asclepio.Usuario.User.User;
import Asclepio.Usuario.User.Repository.UserRepository;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Define o plano da conta (empresa inteira), pelo usuário dono — mesmo jeito do AcessoContaService.
 * Quem chama é o painel SaaS: ao criar a conta e quando o plano do cliente muda lá.
 */
@Service
public class PlanoContaService {

    private final UserRepository userRepository;
    private final UserLojaRepository userLojaRepository;
    private final PlanoRepository planoRepository;

    public PlanoContaService(UserRepository userRepository, UserLojaRepository userLojaRepository,
                             PlanoRepository planoRepository) {
        this.userRepository = userRepository;
        this.userLojaRepository = userLojaRepository;
        this.planoRepository = planoRepository;
    }

    /** O plano precisa existir aqui (o SaaS espelha os planos em /interno/planos). */
    @Transactional(readOnly = true)
    public Plano buscarPlano(Long planoSaasId) {
        return planoRepository.findBySaasId(planoSaasId)
                .orElseThrow(() -> new BusinessException("Plano " + planoSaasId
                        + " não existe no Panel. Sincronize os planos pelo painel SaaS."));
    }

    /** planoSaasId null = tira o plano (empresa fica sem limites). */
    @Transactional
    public void definirPlano(UUID userId, Long planoSaasId) {
        Plano plano = planoSaasId == null ? null : buscarPlano(planoSaasId);

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

        // Entidades gerenciadas: o Hibernate grava ao fim da transação.
        empresas.forEach(empresa -> empresa.setPlano(plano));
    }
}
