package Asclepio.Pedido.Service;

import Asclepio.Estoque.Estoque;
import Asclepio.Pedido.dto.PedidoAddDTO;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoValidator {

    public void validarCriacao(PedidoAddDTO dto) {

        if (dto == null) {
            throw new BusinessException("Dados do pedido são obrigatórios");
        }

        if (dto.lojaId() == null) {
            throw new BusinessException("Loja obrigatória");
        }

        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new BusinessException("Pedido precisa ter itens");
        }

        if (dto.tipoEntrega() == null) {
            throw new BusinessException("Tipo de entrega obrigatório");
        }

        if (dto.formaDePagamento() == null) {
            throw new BusinessException("Forma de pagamento obrigatória");
        }
    }

    public void validarEstoqueDosItens(PedidoAddDTO dto, List<Estoque> estoquesDaLoja) {

        for (var itemDto : dto.itens()) {

            if (itemDto.variacaoId() == null) {
                throw new BusinessException("Variação obrigatória");
            }

            if (itemDto.quantidade() == null || itemDto.quantidade() <= 0) {
                throw new BusinessException("Quantidade inválida");
            }

            Estoque estoque = estoquesDaLoja.stream()
                    .filter(e -> e.getVariacaoId().equals(itemDto.variacaoId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Variação não encontrada no estoque"));

            if (estoque.getQuantidade() < itemDto.quantidade()) {
                throw new BusinessException(
                        "Estoque insuficiente para a variação id: " + itemDto.variacaoId()
                );
            }
        }
    }
}