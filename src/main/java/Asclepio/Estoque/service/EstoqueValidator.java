package Asclepio.Estoque.service;

import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.dto.EstoqueAddDto;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EstoqueValidator {

    private final EstoqueRepository estoqueRepository;

    public EstoqueValidator(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public void validarCriacao(EstoqueAddDto dto) {
        if (dto == null) {
            throw new BusinessException("Dados do estoque são obrigatórios");
        }

        validarLojaInformada(dto);
        validarVariacaoInformada(dto.variacaoId());
        validarQuantidadeObrigatoria(dto.quantidade());
        validarPrecoObrigatorio(dto.precoVenda());
    }

    public void validarLojaInformada(EstoqueAddDto dto) {
        if (dto.lojaID() == null ) {
            throw new BusinessException("Informe lojaID ou nomeLoja");
        }
    }

    public void validarVariacaoInformada(Long variacaoId) {
        if (variacaoId == null) {
            throw new BusinessException("Variação é obrigatória");
        }
    }

    public void validarQuantidadeObrigatoria(Integer quantidade) {
        if (quantidade == null) {
            throw new BusinessException("Quantidade é obrigatória");
        }

        validarQuantidade(quantidade);
    }

    public void validarPrecoObrigatorio(BigDecimal precoVenda) {
        if (precoVenda == null) {
            throw new BusinessException("Preço de venda é obrigatório");
        }

        validarPreco(precoVenda);
    }

    public void validarEstoqueDuplicado(Long lojaId, Long variacaoId) {
        if (estoqueRepository.existsByLoja_IdAndVariacaoId(lojaId, variacaoId)) {
            throw new BusinessException("Já existe estoque para essa loja e variação");
        }
    }

    public void validarQuantidade(Integer quantidade) {
        if (quantidade != null && quantidade < 0) {
            throw new BusinessException("Quantidade inválida");
        }
    }

    public void validarPreco(BigDecimal precoVenda) {
        if (precoVenda != null && precoVenda.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Preço inválido");
        }
    }

    public void validarPercentualPromocao(BigDecimal percentual) {
        if (percentual == null) {
            throw new BusinessException("Percentual obrigatório");
        }

        if (percentual.compareTo(BigDecimal.ZERO) < 0 ||
                percentual.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessException("Percentual deve ser entre 0 e 100");
        }
    }

    private boolean isBlank(String valor) {
        return valor == null || valor.isBlank();
    }
}