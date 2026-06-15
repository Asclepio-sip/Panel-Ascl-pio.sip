package Asclepio.Produto;

import Asclepio.Categoria.Categoria;
import Asclepio.Categoria.CategoriaRepository;
import Asclepio.Produto.Repository.ProductRepository;
import Asclepio.Produto.Repository.ProdutoSpecification;
import Asclepio.Produto.dto.ProductoAddDto;
import Asclepio.Produto.dto.ProductoResponseDto;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Product criar(ProductoAddDto dto) {

        validarCriacao(dto);

        Categoria categoria = categoriaRepository
                .findByNomeCategoria(dto.categoriaNome().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Product product = dto.toDomain();

        product.setCategoria(categoria);

        return repository.save(product);
    }

    public Page<ProductoResponseDto> listarTodos(
            ProdutoFiltro filtro,
            Pageable pageable
    ) {
        return repository
                .findAll(ProdutoSpecification.filtrar(filtro), pageable)
                .map(ProductoResponseDto::fromEntity);
    }

    public Product editar(Long id, ProdutoUpdateDto dto) {

        if (dto == null) {
            throw new BusinessException("Dados para edição do produto são obrigatórios");
        }

        Product product = buscarPorId(id);

        if (dto.name() != null && !dto.name().isBlank()) {
            product.setName(dto.name().trim());
        }

        if (dto.imagemBase64() != null) {
            product.setImagemBase64(dto.imagemBase64());
        }

        if (dto.categoriaNome() != null && !dto.categoriaNome().isBlank()) {

            Categoria categoria = categoriaRepository
                    .findByNomeCategoria(dto.categoriaNome().trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

            product.setCategoria(categoria);
        }

        return repository.save(product);
    }

    public void deletar(Long id) {

        Product product = buscarPorId(id);

        repository.delete(product);
    }

    private Product buscarPorId(Long id) {

        if (id == null) {
            throw new BusinessException("ID do produto é obrigatório");
        }

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    private void validarCriacao(ProductoAddDto dto) {

        if (dto == null) {
            throw new BusinessException("Dados do produto são obrigatórios");
        }

        if (dto.categoriaNome() == null || dto.categoriaNome().isBlank()) {
            throw new BusinessException("Categoria obrigatória");
        }
    }
}