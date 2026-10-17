package Asclepio.Plano;

import Asclepio.Empresa.EmpresaRepository;
import Asclepio.Plano.dto.PlanoResponseDTO;
import Asclepio.Plano.dto.PlanoSyncDTO;
import Asclepio.Plano.dto.RecursoPlanoDTO;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final EmpresaRepository empresaRepository;

    public PlanoService(PlanoRepository planoRepository, EmpresaRepository empresaRepository) {
        this.planoRepository = planoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public List<PlanoResponseDTO> listar() {
        return planoRepository.findAllByOrderByPrecoAscSaasIdAsc().stream()
                .map(PlanoResponseDTO::de)
                .toList();
    }

    /**
     * Cria ou atualiza o plano com esse saasId (idempotente: o SaaS pode reenviar sem duplicar).
     * Se vier como "Mais Popular", os outros perdem o selo (mesma regra do SaaS).
     */
    @Transactional
    public PlanoResponseDTO salvar(Long saasId, PlanoSyncDTO dto) {
        if (saasId == null) {
            throw new BusinessException("saasId é obrigatório.");
        }

        Plano plano = planoRepository.findBySaasId(saasId).orElseGet(() -> new Plano(saasId));

        List<RecursoPlano> recursos = dto.recursos() == null
                ? List.of()
                : dto.recursos().stream().map(RecursoPlanoDTO::paraEntidade).toList();

        plano.atualizar(
                dto.nome().trim(),
                dto.descricao() == null ? "" : dto.descricao().trim(),
                dto.preco(),
                dto.maisPopular(),
                recursos
        );
        plano = planoRepository.save(plano);

        if (plano.isMaisPopular()) {
            Long id = plano.getId();
            planoRepository.findByMaisPopularTrue().stream()
                    .filter(p -> !p.getId().equals(id))
                    .forEach(Plano::tirarMaisPopular);
        }

        return PlanoResponseDTO.de(plano);
    }

    @Transactional
    public void remover(Long saasId) {
        Plano plano = planoRepository.findBySaasId(saasId)
                .orElseThrow(() -> new ResourceNotFoundException("Plano " + saasId + " não encontrado."));
        // Sem isso a FK de TB_EMPRESA impede a exclusão.
        empresaRepository.removerPlano(plano);
        planoRepository.delete(plano);
    }
}
