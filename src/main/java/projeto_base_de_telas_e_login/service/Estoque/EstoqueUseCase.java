package projeto_base_de_telas_e_login.service.Estoque;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.dto.Estoque.EstoqueAddDto;
import projeto_base_de_telas_e_login.model.Preco.Preco;
import projeto_base_de_telas_e_login.model.estoque.Estoque;
import projeto_base_de_telas_e_login.repository.EstoquePorta;
import projeto_base_de_telas_e_login.repository.LojaPorta;
import projeto_base_de_telas_e_login.repository.ProdutoPorta;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EstoqueUseCase {

    private final EstoquePorta estoquePorta;
    private final LojaPorta lojaPorta;
    private final ProdutoPorta produtoPorta;

    public EstoqueUseCase(
            EstoquePorta estoquePorta,
            LojaPorta lojaPorta,
            ProdutoPorta produtoPorta
    ) {
        this.estoquePorta = estoquePorta;
        this.lojaPorta = lojaPorta;
        this.produtoPorta = produtoPorta;
    }


    @Transactional
    public void criar(EstoqueAddDto dto) {

        var loja = dto.lojaID() != null
                ? lojaPorta.buscarPorId(dto.lojaID())
                : lojaPorta.buscarPorNome(dto.nomeLoja());

        var lojaFinal = loja
                .orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        var produto = dto.produtoId() != null
                ? produtoPorta.findById(dto.produtoId())
                : produtoPorta.buscarPorNome(dto.nomeProduto());

        var produtoFinal = produto
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        var existente = estoquePorta.buscarPorLojaEProduto(
                lojaFinal.getId(),
                produtoFinal.getId()
        );

        if (existente.isPresent()) {
            throw new RuntimeException("Produto já existe no estoque dessa loja");
        }
        if(dto.lojaID() == null && dto.nomeLoja() == null){
            throw new RuntimeException("Informe lojaId ou nomeLoja");
        }

        if(dto.produtoId() == null && dto.nomeProduto() == null){
            throw new RuntimeException("Informe produtoId ou nomeProduto");
        }

        Estoque estoque = new Estoque(
                null,
                lojaFinal,
                produtoFinal,
                dto.quantidade(),
                new Preco(dto.precoVenda()),
                BigDecimal.ZERO
        );

        estoquePorta.save(estoque);
    }

    public void atualizar(
            Long lojaId,
            Long produtoId,
            Integer quantidade,
            BigDecimal precoVenda
    ) {

        Estoque estoque = estoquePorta
                .buscarPorLojaEProduto(lojaId, produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        Estoque atualizado = new Estoque(
                estoque.getId(),
                estoque.getLoja(),
                estoque.getProduto(),
                quantidade,
                new Preco(precoVenda),
                estoque.getPercentualDesconto()
        );

        estoquePorta.save(atualizado);
    }
    public void deletar(Long id) {
        estoquePorta.deleteById(id);
    }

    public List<Estoque> buscarPorLoja(Long lojaId) {
        return estoquePorta.buscarPorLoja(lojaId);
    }

    public void aplicarPromocao(
            Long lojaId,
            Long produtoId,
            BigDecimal percentual
    ) {

        Estoque estoque = estoquePorta
                .buscarPorLojaEProduto(lojaId, produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        Estoque atualizado = new Estoque(
                estoque.getId(),
                estoque.getLoja(),
                estoque.getProduto(),
                estoque.getQuantidade(),
                estoque.getPrecoVenda(),
                percentual
        );

        estoquePorta.save(atualizado);
    }
    @Transactional(readOnly = true)
    public List<Estoque> listarTodos() {
        return estoquePorta.listarTodos();
    }

    @Transactional(readOnly = true)
    public List<Estoque> buscarPorNomeLoja(String nomeLoja) {
        return estoquePorta.buscarPorNomeLoja(nomeLoja);
    }

    @Transactional(readOnly = true)
    public List<Estoque> buscarPorNomeProduto(String nomeProduto) {
        return estoquePorta.buscarPorNomeProduto(nomeProduto);
    }

    @Transactional(readOnly = true)
    public List<Estoque> filtrar(Long lojaId, String nomeLoja, Boolean semEstoque) {
        return estoquePorta.filtrar(lojaId, nomeLoja, semEstoque);
    }
}