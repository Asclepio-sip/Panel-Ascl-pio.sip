package Asclepio.Pedido.Service;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.LojaBairro.LojaBairro;
import Asclepio.Loja.LojaBairro.Repository.LojaBairroRepository;
import Asclepio.Pedido.Enum.TipoEntrega;
import Asclepio.Pedido.Pedido;
import Asclepio.Pedido.dto.PedidoAddDTO;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PedidoEntregaService {

    private final LojaBairroRepository lojaBairroRepository;

    public PedidoEntregaService(LojaBairroRepository lojaBairroRepository) {
        this.lojaBairroRepository = lojaBairroRepository;
    }

    public void aplicarEntrega(Pedido pedido, PedidoAddDTO dto, Loja loja) {

        switch (dto.tipoEntrega()) {

            case RETIRADA -> aplicarRetirada(pedido, loja);

            case ENTREGA -> aplicarEntregaComFrete(pedido, dto, loja);
        }

        pedido.calcularTotalFinal();
    }

    private void aplicarRetirada(Pedido pedido, Loja loja) {

        if (!loja.aceitaRetirada()) {
            throw new BusinessException("Esta loja não aceita retirada");
        }

        pedido.setEndereco(null);
        pedido.setBairro(null);
        pedido.setComplemento(null);
        pedido.setValorFrete(BigDecimal.ZERO);
        pedido.setFreteGratis(false);
    }

    private void aplicarEntregaComFrete(Pedido pedido, PedidoAddDTO dto, Loja loja) {

        if (!loja.aceitaEntrega()) {
            throw new BusinessException("Esta loja não realiza entrega");
        }

        if (dto.bairroId() == null) {
            throw new BusinessException("Bairro obrigatório");
        }

        if (dto.endereco() == null || dto.endereco().isBlank()) {
            throw new BusinessException("Endereço obrigatório");
        }

        LojaBairro lojaBairro = lojaBairroRepository
                .findByLoja_IdAndBairro_Id(loja.getId(), dto.bairroId())
                .orElseThrow(() -> new BusinessException("Bairro não atendido"));

        pedido.setEndereco(dto.endereco());
        pedido.setComplemento(dto.complemento());
        pedido.setBairro(lojaBairro.getBairro().getNome());

        BigDecimal subtotalProdutos = pedido.getTotalProdutos();
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
}