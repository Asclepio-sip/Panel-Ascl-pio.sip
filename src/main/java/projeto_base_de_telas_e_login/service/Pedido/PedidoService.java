package projeto_base_de_telas_e_login.service.Pedido;

import org.springframework.stereotype.Service;

import projeto_base_de_telas_e_login.dto.Pedido.PedidoAddDTO;

import projeto_base_de_telas_e_login.model.Pedido.Enum.StatusDoPedido;

import projeto_base_de_telas_e_login.entidade.Estoque;
import projeto_base_de_telas_e_login.repository.CategoriaRepository;
import projeto_base_de_telas_e_login.repository.EstoqueRepository;

import projeto_base_de_telas_e_login.entidade.Loja;
import projeto_base_de_telas_e_login.repository.LojaRepository;

import projeto_base_de_telas_e_login.entidade.Pedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final CategoriaRepository.PedidoRepository repository;
    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;

    public PedidoService(CategoriaRepository.PedidoRepository repository, LojaRepository lojaRepository, EstoqueRepository estoqueRepository) {

        this.repository = repository;
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
    }

    public void criarPedido(PedidoAddDTO dto) {

        Loja loja = lojaRepository.findById(dto.lojaId()).orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        List<Estoque> estoquesDaLoja = estoqueRepository.findByLoja_Id(loja.getId());

        Pedido pedido = dto.toEntity(loja, estoquesDaLoja);

        repository.save(pedido);
    }

    public List<Pedido> listarTodos() {

        return repository.findAll();
    }

    public Pedido buscarPorId(Long id) {

        return repository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> listarPedidosDoDia() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicio = hoje.atStartOfDay();

        LocalDateTime fim = hoje.atTime(23, 59, 59);

        return repository.findByCriadoEmBetweenOrderByCriadoEmDesc(inicio, fim);
    }

    public void atualizarStatusPedido(Long id, StatusDoPedido status) {

        repository.atualizarStatus(id, status);
    }

    public byte[] imprimirPDF(Long id) {

        Pedido pedido = buscarPorId(id);

        return new byte[0];
    }
}