package Asclepio.Produto;

import org.springframework.stereotype.Service;
import Asclepio.Categoria.Categoria;
import Asclepio.Categoria.CategoriaRepository;
import Asclepio.Produto.Repository.ProductRepository;
import Asclepio.Produto.Repository.ProdutoSpecification;
import Asclepio.Produto.dto.ProductoAddDto;
import Asclepio.Produto.dto.ProductoResponseDto;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoUpdateDto;
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