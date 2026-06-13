package projeto_base_de_telas_e_login.Produto;

import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.Categoria.Categoria;
import projeto_base_de_telas_e_login.Categoria.CategoriaRepository;
import projeto_base_de_telas_e_login.Produto.Repository.ProductRepository;
import projeto_base_de_telas_e_login.Produto.Repository.ProdutoSpecification;
import projeto_base_de_telas_e_login.Produto.dto.ProductoAddDto;
import projeto_base_de_telas_e_login.Produto.dto.ProductoResponseDto;
import projeto_base_de_telas_e_login.Produto.dto.ProdutoFiltro;
import projeto_base_de_telas_e_login.Produto.dto.ProdutoUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProdutoService {

    private final ProductRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProductRepository repository, CategoriaRepository categoriaRepository) {

        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public Product criar(ProductoAddDto dto) {

        if (dto.categoriaNome() == null || dto.categoriaNome().isBlank()) {

            throw new RuntimeException("Categoria obrigatória");
        }

        Categoria categoria = categoriaRepository.findByNomeCategoria(dto.categoriaNome()).orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Product product = dto.toDomain();

        product.setCategoria(categoria);

        return repository.save(product);
    }

    public Page<ProductoResponseDto> listarTodos(ProdutoFiltro filtro, Pageable pageable) {
        return repository.findAll(ProdutoSpecification.filtrar(filtro), pageable).map(ProductoResponseDto::fromEntity);
    }


    public Product editar(Long id, ProdutoUpdateDto dto) {

        Product product = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (dto.name() != null) {
            product.setName(dto.name());
        }


        if (dto.imagemBase64() != null) {
            product.setImagemBase64(dto.imagemBase64());
        }

        if (dto.categoriaNome() != null) {

            Categoria categoria = categoriaRepository.findByNomeCategoria(dto.categoriaNome()).orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            product.setCategoria(categoria);
        }

        return repository.save(product);
    }

    public void deletar(Long id) {

        Product product = repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        repository.delete(product);
    }
}