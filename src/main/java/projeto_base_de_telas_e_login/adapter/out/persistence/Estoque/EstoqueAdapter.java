package projeto_base_de_telas_e_login.adapter.out.persistence.Estoque;

import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Estoque.ProdutoMenorPrecoDTO;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.loja.LojaEntity;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.loja.LojaRepository;
import projeto_base_de_telas_e_login.adapter.out.persistence.Product.ProductEntity;
import projeto_base_de_telas_e_login.adapter.out.persistence.Product.ProductRepository;
import projeto_base_de_telas_e_login.domain.model.Preco.Preco;
import projeto_base_de_telas_e_login.domain.model.estoque.Estoque;
import projeto_base_de_telas_e_login.domain.repository.EstoquePorta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class EstoqueAdapter implements EstoquePorta {

    private final EstoqueRepository repository;
    private final LojaRepository lojaRepository;
    private final ProductRepository productRepository;

    public EstoqueAdapter(
            EstoqueRepository repository,
            LojaRepository lojaRepository,
            ProductRepository productRepository
    ) {
        this.repository = repository;
        this.lojaRepository = lojaRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void save(Estoque estoque) {

        LojaEntity loja = lojaRepository.getReferenceById(
                estoque.getLoja().getId()
        );

        ProductEntity produto = productRepository.getReferenceById(
                estoque.getProduto().getId()
        );

        EstoqueEntity entity = new EstoqueEntity(
                loja,
                produto,
                estoque.getQuantidade(),
                estoque.getPrecoVenda().getValor(),
                estoque.getPercentualDesconto()
        );

        repository.save(entity);
    }

    @Override
    public Optional<Estoque> buscarPorLojaEProduto(Long lojaId, Long produtoId) {
        return repository.findByLoja_IdAndProduto_Id(lojaId, produtoId)
                .map(this::toDomain);
    }

    @Override
    public List<Estoque> buscarPorLoja(Long lojaId) {
        return repository.findByLoja_Id(lojaId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }



    private Estoque toDomain(EstoqueEntity entity) {

        BigDecimal desconto = entity.getPercentualDesconto() != null
                ? entity.getPercentualDesconto()
                : BigDecimal.ZERO;

        return new Estoque(
                entity.getId(),
                entity.getLoja().toDomain(),
                entity.getProduto().toDomain(),
                entity.getQuantidade(),
                new Preco(entity.getPrecoVenda()),
                desconto
        );
    }

    @Override
    public List<Estoque> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Estoque> buscarPorNomeLoja(String nomeLoja) {
        return repository.findByLoja_NomeContainingIgnoreCase(nomeLoja)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Estoque> buscarPorNomeProduto(String nomeProduto) {
        return repository.findByProduto_NameContainingIgnoreCase(nomeProduto)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Estoque> filtrar(Long lojaId, String nomeLoja, Boolean semEstoque) {
        return repository.filtrar(lojaId, nomeLoja, semEstoque)
                .stream()
                .map(this::toDomain)
                .toList();
    }

}