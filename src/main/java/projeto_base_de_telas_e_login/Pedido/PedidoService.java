package projeto_base_de_telas_e_login.Pedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import projeto_base_de_telas_e_login.Loja.LojaBairro.LojaBairro;
import projeto_base_de_telas_e_login.Loja.LojaBairro.LojaBairroRepository;
import projeto_base_de_telas_e_login.Pedido.Enum.TipoEntrega;
import projeto_base_de_telas_e_login.Pedido.dto.PedidoAddDTO;
import org.springframework.transaction.annotation.Transactional;
import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;

import projeto_base_de_telas_e_login.Estoque.Estoque;
import projeto_base_de_telas_e_login.Estoque.Repository.EstoqueRepository;

import projeto_base_de_telas_e_login.Loja.Loja.Loja;
import projeto_base_de_telas_e_login.Loja.Loja.LojaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;
    private final LojaBairroRepository lojaBairroRepository;

    public PedidoService(PedidoRepository pedidoRepository, LojaRepository lojaRepository, EstoqueRepository estoqueRepository, LojaBairroRepository lojaBairroRepository) {

        this.pedidoRepository = pedidoRepository;
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
        this.lojaBairroRepository = lojaBairroRepository;
    }

    @Transactional
    public void criarPedido(PedidoAddDTO dto) {

        Loja loja = lojaRepository.findById(dto.lojaId()).orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        List<Estoque> estoquesDaLoja = estoqueRepository.findByLoja_Id(loja.getId());

        if (dto.itens() == null || dto.itens().isEmpty()) {

            throw new RuntimeException("Pedido precisa ter itens");
        }

        if (dto.tipoEntrega() == null) {

            throw new RuntimeException("Tipo de entrega obrigatório");
        }

        for (var itemDto : dto.itens()) {

            if (itemDto.quantidade() <= 0) {

                throw new RuntimeException("Quantidade inválida");
            }

            Estoque estoque = estoquesDaLoja.stream().filter(e -> e.getProduto().getId().equals(itemDto.produtoId())).findFirst().orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque"));

            if (estoque.getQuantidade() < itemDto.quantidade()) {

                throw new RuntimeException("Estoque insuficiente para: " + estoque.getProduto().getName());
            }
        }
        if (dto.formaDePagamento() == null) {
            throw new RuntimeException("Forma de pagamento obrigatória");
        }
        Pedido pedido = dto.toEntity(loja, estoquesDaLoja);

        pedido.calcularSubtotalProdutos();

        switch (dto.tipoEntrega()) {

            case RETIRADA -> {

                if (!loja.aceitaRetirada()) {

                    throw new RuntimeException("Esta loja não aceita retirada");
                }

                pedido.setEndereco(null);
                pedido.setBairro(null);
                pedido.setComplemento(null);

                pedido.setValorFrete(BigDecimal.ZERO);

                pedido.setFreteGratis(false);
            }

            case ENTREGA -> {

                if (!loja.aceitaEntrega()) {

                    throw new RuntimeException("Esta loja não realiza entrega");
                }
                if (dto.endereco() == null || dto.endereco().isBlank()) {
                    throw new RuntimeException("Endereço obrigatório");
                }

                LojaBairro lojaBairro = lojaBairroRepository.findByLoja_IdAndBairro_Id(loja.getId(), dto.bairroId()).orElseThrow(() -> new RuntimeException("Bairro não atendido"));
                pedido.setEndereco(dto.endereco());

                pedido.setComplemento(dto.complemento());

                pedido.setBairro(lojaBairro.getBairro().getNome());

                BigDecimal subtotalProdutos = pedido.getTotalProdutos();

                BigDecimal minimoFreteGratis = loja.getValorMinimoFreteGratis();

                System.out.println(
                        "Min frete gratis: "
                                + loja.getValorMinimoFreteGratis()
                );

                System.out.println(
                        "Subtotal: "
                                + subtotalProdutos
                );

                boolean freteGratis = minimoFreteGratis != null && subtotalProdutos.compareTo(minimoFreteGratis) >= 0;

                if (freteGratis) {

                    pedido.setValorFrete(BigDecimal.ZERO);

                    pedido.setFreteGratis(true);

                } else {

                    pedido.setValorFrete(lojaBairro.getValorFrete());

                    pedido.setFreteGratis(false);
                }
            }
        }

        pedido.calcularTotalFinal();

        for (var item : pedido.getItens()) {

            Estoque estoque = estoquesDaLoja.stream().filter(e -> e.getProduto().getId().equals(item.getProdutoId())).findFirst().orElseThrow();

            estoque.baixarEstoque(item.getQuantidade());

            estoqueRepository.save(estoque);
        }

        pedidoRepository.save(pedido);
    }


    public Page<Pedido> listarTodos(Pageable pageable) {

        return pedidoRepository.findAll(pageable);
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