package Asclepio.Pedido.Service;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.LojaBairro.LojaBairro;
import Asclepio.Loja.LojaBairro.Repository.LojaBairroRepository;
import Asclepio.Pedido.Pedido;
import Asclepio.Pedido.dto.pedido.PedidoAddDTO;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PedidoAtendimentoService {

    private final LojaBairroRepository lojaBairroRepository;

    public PedidoAtendimentoService(LojaBairroRepository lojaBairroRepository) {
        this.lojaBairroRepository = lojaBairroRepository;
    }

    public void aplicarAtendimento(Pedido pedido, PedidoAddDTO dto, Loja loja) {

        if (dto.tipoAtendimentoPedido() == null) {
            throw new BusinessException("Tipo de atendimento do pedido é obrigatório");
        }

        pedido.setTipoAtendimentoPedido(dto.tipoAtendimentoPedido());

        switch (dto.tipoAtendimentoPedido()) {

            case BALCAO -> aplicarBalcao(pedido);

            case RETIRADA_NA_LOJA -> aplicarRetiradaNaLoja(pedido, loja);

            case ENTREGA_PROPRIA -> aplicarEntregaPropria(pedido, dto, loja);

            case ENTREGA_TERCEIRIZADA -> aplicarEntregaTerceirizada();
        }

        pedido.calcularTotalFinal();
    }

    private void aplicarBalcao(Pedido pedido) {
        pedido.zerarEntrega();
    }

    private void aplicarRetiradaNaLoja(Pedido pedido, Loja loja) {

        if (!loja.aceitaRetirada()) {
            throw new BusinessException("Esta loja não aceita retirada");
        }

        pedido.zerarEntrega();
    }

    private void aplicarEntregaPropria(Pedido pedido, PedidoAddDTO dto, Loja loja) {

        if (!loja.aceitaEntrega()) {
            throw new BusinessException("Esta loja não realiza entrega própria");
        }

        if (dto.bairroId() == null) {
            throw new BusinessException("Bairro obrigatório para entrega própria");
        }

        if (dto.endereco() == null || dto.endereco().isBlank()) {
            throw new BusinessException("Endereço obrigatório para entrega própria");
        }

        LojaBairro lojaBairro = lojaBairroRepository
                .findByLoja_IdAndBairro_Id(loja.getId(), dto.bairroId())
                .orElseThrow(() -> new BusinessException("Bairro não atendido por esta loja"));

        pedido.setEndereco(dto.endereco());
        pedido.setComplemento(dto.complemento());
        pedido.setBairro(lojaBairro.getBairro().getNome());

        BigDecimal subtotalProdutos = pedido.getTotalProdutos() != null
                ? pedido.getTotalProdutos()
                : BigDecimal.ZERO;

        BigDecimal minimoFreteGratis = loja.getValorMinimoFreteGratis();

        boolean freteGratis = minimoFreteGratis != null
                && subtotalProdutos.compareTo(minimoFreteGratis) >= 0;

        if (freteGratis) {
            pedido.setValorFrete(BigDecimal.ZERO);
            pedido.setFreteGratis(true);
        } else {
            pedido.setValorFrete(lojaBairro.getValorFrete());
            pedido.setFreteGratis(false);
        }
    }

    private void aplicarEntregaTerceirizada() {
        throw new BusinessException("Entrega terceirizada ainda não implementada");
    }

    public void aplicarAtendimento(Pedido pedido, Loja loja) {

        switch (pedido.getTipoAtendimentoPedido()) {

            case BALCAO -> aplicarBalcao(pedido);

            case RETIRADA_NA_LOJA -> aplicarRetiradaNaLoja(pedido, loja);

            case ENTREGA_TERCEIRIZADA -> aplicarEntregaTerceirizada();

            case ENTREGA_PROPRIA ->
                    throw new BusinessException("Entrega própria requer PedidoAddDTO");
        }

        pedido.calcularTotalFinal();
    }
}