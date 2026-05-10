package projeto_base_de_telas_e_login.service.Produto;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.persistence.Product.Product;
import projeto_base_de_telas_e_login.persistence.Product.ProductRepository;
import projeto_base_de_telas_e_login.persistence.categoria.Categoria;
import projeto_base_de_telas_e_login.persistence.categoria.CategoriaRepository;

import java.util.List;

@Service
public class ProdutoService {

    private final ProductRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(
            ProductRepository repository,
            CategoriaRepository categoriaRepository
    ) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public Product criar(Product product) {

        if (product.getCategoria() == null ||
                product.getCategoria().getNomeCategoria() == null) {

            throw new RuntimeException("Categoria obrigatória");
        }

        Categoria categoria = categoriaRepository
                .findByNomeCategoria(
                        product.getCategoria().getNomeCategoria()
                )
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada")
                );

        product.setCategoria(categoria);

        return repository.save(product);
    }

    public List<Product> listarTodos() {
        return repository.findAll();
    }

    public List<Product> listarPorCategoria(String nomeCategoria) {

        return repository.findByCategoria_NomeCategoria(nomeCategoria);
    }

    public Product editar(Long id, Product atualizado) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Produto não encontrado")
                );

        product.setName(atualizado.getName());
        product.setVariacao(atualizado.getVariacao());
        product.setImagemBase64(atualizado.getImagemBase64());

        if (atualizado.getCategoria() != null &&
                atualizado.getCategoria().getNomeCategoria() != null) {

            Categoria categoria = categoriaRepository
                    .findByNomeCategoria(
                            atualizado.getCategoria().getNomeCategoria()
                    )
                    .orElseThrow(() ->
                            new RuntimeException("Categoria não encontrada")
                    );

            product.setCategoria(categoria);
        }

        return repository.save(product);
    }

    public void deletar(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Produto não encontrado")
                );

        repository.delete(product);
    }
}