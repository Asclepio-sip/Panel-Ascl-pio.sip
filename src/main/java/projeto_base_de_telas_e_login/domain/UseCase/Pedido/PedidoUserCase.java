package projeto_base_de_telas_e_login.domain.UseCase.Pedido;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido.ItemPedidoAddDTO;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido.PedidoAddDTO;
import projeto_base_de_telas_e_login.adapter.out.persistence.Estoque.EstoqueEntity;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;
import projeto_base_de_telas_e_login.domain.model.product.Product;
import projeto_base_de_telas_e_login.domain.repository.EstoquePorta;
import projeto_base_de_telas_e_login.domain.repository.GeradorPdfPort;
import projeto_base_de_telas_e_login.domain.repository.PedidoPorta;
import projeto_base_de_telas_e_login.domain.repository.ProdutoPorta;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoUserCase {

    private final PedidoPorta pedidoPorta;
    private final ProdutoPorta produtoPorta;
    private final GeradorPdfPort geradorPdfPort;
//    private final EstoquePorta estoquePorta;

    public PedidoUserCase(
            PedidoPorta pedidoPorta,
            ProdutoPorta produtoPorta,
            GeradorPdfPort geradorPdfPort
    ) {
        this.pedidoPorta = pedidoPorta;
        this.produtoPorta = produtoPorta;
        this.geradorPdfPort = geradorPdfPort;
    }

//    public void criarPedido(PedidoAddDTO dto) {
//// Pega todos os ids dos produtos do pedido
//        var ids = dto.itens().stream()
//                .map(ItemPedidoAddDTO::produtoId)
//                .toList();
//
//        List<EstoqueEntity> estoquesDaLoja = estoquePorta.findByLojaIdAndProdutoIds(dto.lojaId(), ids);
//
//        if (estoquesDaLoja.size() != ids.size()) {
//            throw new IllegalArgumentException("Um ou mais produtos não existem no estoque da loja");
//        }
//
//// Agora passa a lista correta para o DTO
//        Pedido pedido = dto.toDomain(estoquesDaLoja);
//
//// Salva o pedido
//        pedidoPorta.save(pedido);
//    }

    public List<Pedido> listarTodos() {

        LocalDate hoje = LocalDate.now();

        return pedidoPorta.findByCriadoBetweenOrderByCriadoDesc(
                hoje.atStartOfDay(),
                hoje.atTime(23, 59, 59)
        );
    }

    public void atualizarStatusPedido(Long id, StatusDoPedido status) {
        pedidoPorta.atualizarStatus(id, status);
    }

    public byte[] imprimirPDF(Long id) {

        Pedido pedido = pedidoPorta.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return geradorPdfPort.gerarPdfPedido(pedido);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoPorta.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}