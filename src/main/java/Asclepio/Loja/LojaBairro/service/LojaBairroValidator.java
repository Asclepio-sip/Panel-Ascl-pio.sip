package Asclepio.Loja.LojaBairro.service;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.LojaBairro.Repository.LojaBairroRepository;
import Asclepio.Loja.LojaBairro.dto.CreateLojaBairroRequest;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LojaBairroValidator {

    private final LojaBairroRepository lojaBairroRepository;

    public LojaBairroValidator(LojaBairroRepository lojaBairroRepository) {
        this.lojaBairroRepository = lojaBairroRepository;
    }

    public void validarCriacao(CreateLojaBairroRequest request) {

        if (request == null) {
            throw new BusinessException("Dados do vínculo entre loja e bairro são obrigatórios");
        }

        if (request.lojaId() == null) {
            throw new BusinessException("Loja é obrigatória");
        }

        if (request.bairroId() == null) {
            throw new BusinessException("Bairro é obrigatório");
        }

        validarValorFrete(request.valorFrete());
    }

    public void validarValorFrete(BigDecimal valorFrete) {

        if (valorFrete == null) {
            throw new BusinessException("Valor do frete é obrigatório");
        }

        if (valorFrete.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Valor do frete não pode ser negativo");
        }
    }

    public void validarLojaAceitaEntrega(Loja loja) {

        if (!loja.aceitaEntrega()) {
            throw new BusinessException("Essa loja não aceita entrega, então não pode ter bairros vinculados");
        }
    }

    public void validarBairroNaoVinculado(Long lojaId, Long bairroId) {

        if (lojaBairroRepository.existsByLoja_IdAndBairro_Id(lojaId, bairroId)) {
            throw new BusinessException("Esse bairro já está vinculado a essa loja");
        }
    }

    public void validarId(Long id) {

        if (id == null) {
            throw new BusinessException("ID do vínculo é obrigatório");
        }
    }
}