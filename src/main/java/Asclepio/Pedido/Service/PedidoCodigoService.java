package Asclepio.Pedido.Service;

import Asclepio.Pedido.Repository.PedidoRepository;
import Asclepio.config.security.CodigoRastreioGenerator;
import org.springframework.stereotype.Service;

@Service
public class PedidoCodigoService {

    private final PedidoRepository pedidoRepository;

    public PedidoCodigoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public String gerarCodigoRastreioUnico() {

        String codigo;

        do {
            codigo = CodigoRastreioGenerator.gerar();
        } while (pedidoRepository.existsByCodigoRastreio(codigo));

        return codigo;
    }
}