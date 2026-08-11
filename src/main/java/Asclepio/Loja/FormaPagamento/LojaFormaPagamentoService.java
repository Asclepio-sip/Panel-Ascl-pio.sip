package Asclepio.Loja.FormaPagamento;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LojaFormaPagamentoService {

    private final LojaFormaPagamentoRepository repository;
    private final LojaRepository lojaRepository;

    public LojaFormaPagamentoService(
            LojaFormaPagamentoRepository repository,
            LojaRepository lojaRepository
    ) {
        this.repository = repository;
        this.lojaRepository = lojaRepository;
    }

    public List<LojaFormaPagamentoDTO> listar(Long lojaId) {

        return repository.findAllByLoja_IdOrderByFormaPagamento(lojaId)
                .stream()
                .map(fp -> new LojaFormaPagamentoDTO(
                        fp.getFormaPagamento(),
                        fp.getFormaPagamento().getDescricao(),
                        fp.getAtivo()
                ))
                .toList();
    }

    @Transactional
    public void atualizar(Long lojaId, List<LojaFormaPagamentoDTO> formas) {

        Loja loja = lojaRepository.findById(lojaId)
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada."));

        for (LojaFormaPagamentoDTO dto : formas) {

            LojaFormaPagamento forma = repository
                    .findByLojaAndFormaPagamento(loja, dto.formaPagamento())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Forma de pagamento não encontrada."
                    ));

            forma.setAtivo(dto.ativo());
        }
    }

    public void validarFormaPagamento(
            Loja loja,
            FormaDePagamento formaPagamento
    ) {

        boolean ativo = repository
                .findByLojaAndFormaPagamento(loja, formaPagamento)
                .map(LojaFormaPagamento::getAtivo)
                .orElse(false);


        if (!ativo) {
            throw new BusinessException(
                    "A forma de pagamento "
                            + formaPagamento
                            + " não está disponível nesta loja."
            );
        }
    }
}