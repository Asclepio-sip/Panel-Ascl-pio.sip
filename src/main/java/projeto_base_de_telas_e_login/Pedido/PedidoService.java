package projeto_base_de_telas_e_login.Pedido;

import org.springframework.stereotype.Service;

import projeto_base_de_telas_e_login.Pedido.dto.PedidoAddDTO;

import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;

import projeto_base_de_telas_e_login.Estoque.Estoque;
import projeto_base_de_telas_e_login.Categoria.CategoriaRepository;
import projeto_base_de_telas_e_login.Estoque.EstoqueRepository;

import projeto_base_de_telas_e_login.Loja.Loja.Loja;
import projeto_base_de_telas_e_login.Loja.Loja.LojaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;

    public PedidoService(PedidoRepository pedidoRepository, LojaRepository lojaRepository, EstoqueRepository estoqueRepository) {

        this.pedidoRepository = pedidoRepository;
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
    }

    public void criarPedido(PedidoAddDTO dto) {

        Loja loja = lojaRepository.findById(dto.lojaId()).orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        List<Estoque> estoquesDaLoja = estoqueRepository.findByLoja_Id(loja.getId());

        Pedido pedido = dto.toEntity(loja, estoquesDaLoja);

        pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {

        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {

        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> listarPedidosDoDia() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicio = hoje.atStartOfDay();

        LocalDateTime fim = hoje.atTime(23, 59, 59);

        return pedidoRepository.findByCriadoEmBetweenOrderByCriadoEmDesc(inicio, fim);
    }

    public void atualizarStatusPedido(Long id, StatusDoPedido status) {

        pedidoRepository.atualizarStatus(id, status);
    }

    public byte[] imprimirPDF(Long id) {

        Pedido pedido = buscarPorId(id);

        return new byte[0];
    }
}