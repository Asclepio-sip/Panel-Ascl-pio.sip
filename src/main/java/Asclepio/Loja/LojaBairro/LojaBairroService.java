package Asclepio.Loja.LojaBairro;

import Asclepio.Loja.Bairro.Bairro;
import Asclepio.Loja.Bairro.Repository.BairroRepository;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Loja.LojaBairro.Repository.LojaBairroRepository;
import Asclepio.Loja.LojaBairro.Repository.LojaBairroSpecification;
import Asclepio.Loja.LojaBairro.dto.CreateLojaBairroRequest;
import Asclepio.Loja.LojaBairro.dto.LojaBairroFiltroDTO;
import Asclepio.Loja.LojaBairro.dto.LojaBairroResponse;
import Asclepio.Loja.LojaBairro.service.LojaBairroValidator;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class LojaBairroService {

    private final LojaBairroRepository lojaBairroRepository;
    private final LojaRepository lojaRepository;
    private final BairroRepository bairroRepository;
    private final LojaBairroValidator validator;

    public LojaBairroService(
            LojaBairroRepository lojaBairroRepository,
            LojaRepository lojaRepository,
            BairroRepository bairroRepository,
            LojaBairroValidator validator
    ) {
        this.lojaBairroRepository = lojaBairroRepository;
        this.lojaRepository = lojaRepository;
        this.bairroRepository = bairroRepository;
        this.validator = validator;
    }

    @Transactional
    public LojaBairroResponse criar(CreateLojaBairroRequest request) {

        validator.validarCriacao(request);

        Loja loja = buscarLoja(request.lojaId());

        Bairro bairro = buscarBairro(request.bairroId());

        validator.validarLojaAceitaEntrega(loja);

        validator.validarBairroNaoVinculado(loja.getId(), bairro.getId());

        LojaBairro lojaBairro = new LojaBairro(
                null,
                loja,
                bairro,
                request.valorFrete()
        );

        LojaBairro salvo = lojaBairroRepository.save(lojaBairro);

        return LojaBairroResponse.fromEntity(salvo);
    }

    public Page<LojaBairroResponse> listar(LojaBairroFiltroDTO filtro, Pageable pageable) {
        return lojaBairroRepository
                .findAll(LojaBairroSpecification.filtrar(filtro), pageable)
                .map(LojaBairroResponse::fromEntity);
    }

    @Transactional
    public LojaBairroResponse atualizarFrete(Long id, BigDecimal valorFrete) {

        validator.validarId(id);
        validator.validarValorFrete(valorFrete);

        LojaBairro lojaBairro = buscarPorId(id);

        lojaBairro.atualizarFrete(valorFrete);

        LojaBairro atualizado = lojaBairroRepository.save(lojaBairro);

        return LojaBairroResponse.fromEntity(atualizado);
    }

    @Transactional
    public void deletar(Long id) {

        validator.validarId(id);

        LojaBairro lojaBairro = buscarPorId(id);

        lojaBairroRepository.delete(lojaBairro);
    }

    private Loja buscarLoja(Long lojaId) {
        return lojaRepository.findById(lojaId)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
    }

    private Bairro buscarBairro(Long bairroId) {
        return bairroRepository.findById(bairroId)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado"));
    }

    private LojaBairro buscarPorId(Long id) {
        return lojaBairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo entre loja e bairro não encontrado"));
    }
}