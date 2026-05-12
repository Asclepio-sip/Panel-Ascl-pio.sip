package projeto_base_de_telas_e_login.Estoque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projeto_base_de_telas_e_login.Estoque.dto.EstoqueAddDto;
import projeto_base_de_telas_e_login.Estoque.dto.EstoqueCardDto;
import projeto_base_de_telas_e_login.Loja.Loja.LojaRepository;
import projeto_base_de_telas_e_login.Produto.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final LojaRepository lojaRepository;
    private final ProductRepository productRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, LojaRepository lojaRepository, ProductRepository productRepository) {
        this.estoqueRepository = estoqueRepository;
        this.lojaRepository = lojaRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void criar(EstoqueAddDto dto) {


        if (dto.lojaID() == null && dto.nomeLoja() == null) {
            throw new RuntimeException("Informe lojaID ou nomeLoja");
        }

        var loja = dto.lojaID() != null
                ? lojaRepository.findById(dto.lojaID())
                : lojaRepository.findByNome(dto.nomeLoja());

        System.out.println("Loja encontrada? " + loja.isPresent());

        var lojaFinal = loja.orElseThrow(() ->
                new RuntimeException("Loja não encontrada"));

        var produto = dto.produtoId() != null
                ? productRepository.findById(dto.produtoId())
                : productRepository.findByName(dto.nomeProduto());

        System.out.println("Produto encontrado? " + produto.isPresent());

        var produtoFinal = produto.orElseThrow(() ->
                new RuntimeException("Produto não encontrado"));

        var existente = estoqueRepository
                .findByLoja_IdAndProduto_Id(
                        lojaFinal.getId(),
                        produtoFinal.getId()
                );

        System.out.println("Produto já existe no estoque? " + existente.isPresent());

        if (existente.isPresent()) {
            throw new RuntimeException("Produto já existe no estoque");
        }

        Estoque estoque = new Estoque(
                null,
                lojaFinal,
                produtoFinal,
                dto.quantidade(),
                dto.precoVenda(),
                BigDecimal.ZERO
        );

        estoqueRepository.save(estoque);

        System.out.println("ESTOQUE SALVO COM SUCESSO");
    }


    public void atualizar(Long lojaId, Long produtoId, Integer quantidade, BigDecimal precoVenda) {

        Estoque estoque = estoqueRepository.findByLoja_IdAndProduto_Id(lojaId, produtoId).orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        Estoque atualizado = new Estoque(estoque.getId(), estoque.getLoja(), estoque.getProduto(), quantidade, precoVenda, estoque.getPercentualDesconto());

        estoqueRepository.save(atualizado);
    }

    public void deletar(Long id) {
        estoqueRepository.deleteById(id);
    }

    public List<Estoque> buscarPorLoja(Long lojaId) {
        return estoqueRepository.findByLoja_Id(lojaId);
    }

    public void aplicarPromocao(Long lojaId, Long produtoId, BigDecimal percentual) {

        Estoque estoque = estoqueRepository.findByLoja_IdAndProduto_Id(lojaId, produtoId).orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        Estoque atualizado = new Estoque(estoque.getId(), estoque.getLoja(), estoque.getProduto(), estoque.getQuantidade(), estoque.getPrecoVenda(), percentual);

        estoqueRepository.save(atualizado);
    }

    @Transactional(readOnly = true)
    public List<Estoque> lista() {
        return estoqueRepository.findAll();
    }

    public List<EstoqueCardDto> listarTodos() {
        return estoqueRepository.findAll()
                .stream()
                .map(EstoqueCardDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Estoque> buscarPorNomeLoja(String nomeLoja) {
        return estoqueRepository.findByLoja_NomeContainingIgnoreCase(nomeLoja);
    }

    @Transactional(readOnly = true)
    public List<Estoque> buscarPorNomeProduto(String nomeProduto) {
        return estoqueRepository.findByProduto_NameContainingIgnoreCase(nomeProduto);
    }

    @Transactional(readOnly = true)
    public List<Estoque> filtrar(Long lojaId, String nomeLoja, Boolean semEstoque) {
        return estoqueRepository.filtrar(lojaId, nomeLoja, semEstoque);
    }
}