package Asclepio.Plano;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaRepository;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Plano.dto.PlanoEmpresaResponseDTO;
import Asclepio.Plano.dto.PlanoEmpresaResponseDTO.UsoLimiteDTO;
import Asclepio.UserLoja.UserLojaRepository;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aplica o plano da empresa: limite de lojas, de funcionários e recursos liberados (ver RegrasPlano).
 * Os services chamam os métodos validar.../exigir... antes de criar; o erro é BusinessException (400) com a mensagem
 * para o usuário. Não use 403 aqui: o front trata 401/403 como sessão expirada.
 */
@Service
public class LimitePlanoService {

    private final EmpresaRepository empresaRepository;
    private final LojaRepository lojaRepository;
    private final UserLojaRepository userLojaRepository;

    public LimitePlanoService(EmpresaRepository empresaRepository, LojaRepository lojaRepository,
                              UserLojaRepository userLojaRepository) {
        this.empresaRepository = empresaRepository;
        this.lojaRepository = lojaRepository;
        this.userLojaRepository = userLojaRepository;
    }

    @Transactional(readOnly = true)
    public PlanoEmpresaResponseDTO resumo(Long empresaId) {
        Plano plano = plano(empresaId);
        return new PlanoEmpresaResponseDTO(
                plano == null ? null : plano.getNome(),
                usoLojas(empresaId, plano),
                usoFuncionarios(empresaId, plano),
                RegrasPlano.inclui(plano, RegrasPlano.CADASTRO_CLIENTES)
        );
    }

    @Transactional(readOnly = true)
    public void validarNovaLoja(Long empresaId) {
        Plano plano = plano(empresaId);
        UsoLimiteDTO uso = usoLojas(empresaId, plano);
        if (uso.atingido()) {
            throw new BusinessException("Seu plano " + plano.getNome() + " permite até " + uso.limite()
                    + " loja(s) e você já tem " + uso.usado() + ". Para cadastrar mais, troque de plano.");
        }
    }

    @Transactional(readOnly = true)
    public void validarNovoFuncionario(Long empresaId) {
        Plano plano = plano(empresaId);
        UsoLimiteDTO uso = usoFuncionarios(empresaId, plano);
        if (uso.atingido()) {
            throw new BusinessException("Seu plano " + plano.getNome() + " permite até " + uso.limite()
                    + " usuário(s) e você já tem " + uso.usado() + ". Para adicionar mais, troque de plano.");
        }
    }

    @Transactional(readOnly = true)
    public void exigirCadastroClientes(Long empresaId) {
        Plano plano = plano(empresaId);
        if (!RegrasPlano.inclui(plano, RegrasPlano.CADASTRO_CLIENTES)) {
            throw new BusinessException("Seu plano " + plano.getNome()
                    + " não inclui o cadastro de clientes. Para usar, troque de plano.");
        }
    }

    private UsoLimiteDTO usoLojas(Long empresaId, Plano plano) {
        return UsoLimiteDTO.de(lojaRepository.countByEmpresa_Id(empresaId),
                RegrasPlano.limite(plano, RegrasPlano.LOJAS));
    }

    private UsoLimiteDTO usoFuncionarios(Long empresaId, Plano plano) {
        return UsoLimiteDTO.de(userLojaRepository.contarFuncionariosAtivos(empresaId),
                RegrasPlano.limite(plano, RegrasPlano.FUNCIONARIOS));
    }

    private Plano plano(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));
        return empresa.getPlano();
    }
}
