package Asclepio.Categoria;

import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    private static final Set<String> CATEGORIAS_PROTEGIDAS = Set.of(
            "Medicamentos",
            "Beleza",
            "Higiene",
            "Infantil",
            "Vitaminas",
            "Promoções"
    );

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }

    public List<Categoria> listarCategoriasPrincipais() {
        return repository.findByCategoriaPaiIsNull();
    }

    public List<Categoria> listarSubcategorias(Long categoriaPaiId) {
        return repository.findByCategoriaPaiId(categoriaPaiId);
    }

    public Categoria criar(CriarCategoria dto) {

        validarNome(dto.nomeCategoria());

        repository.findByNomeCategoria(dto.nomeCategoria().trim())
                .ifPresent(c -> {
                    throw new BusinessException("Categoria já existe");
                });

        Categoria categoriaPai = null;

        if (dto.categoriaPaiId() != null) {
            categoriaPai = buscarPorId(dto.categoriaPaiId());
        }

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.nomeCategoria().trim());
        categoria.setDescricao(dto.descricao());
        categoria.setIcone(dto.icone());
        categoria.setCategoriaPai(categoriaPai);
        categoria.setAtiva(true);

        return repository.save(categoria);
    }

    public Categoria editar(Long id, CriarCategoria dto) {

        validarNome(dto.nomeCategoria());

        Categoria categoria = buscarPorId(id);

        validarCategoriaProtegida(categoria);

        Categoria categoriaPai = null;

        if (dto.categoriaPaiId() != null) {
            categoriaPai = buscarPorId(dto.categoriaPaiId());

            if (categoriaPai.getId().equals(categoria.getId())) {
                throw new BusinessException("A categoria não pode ser pai dela mesma");
            }
        }

        categoria.setNomeCategoria(dto.nomeCategoria().trim());
        categoria.setDescricao(dto.descricao());
        categoria.setIcone(dto.icone());
        categoria.setCategoriaPai(categoriaPai);

        return repository.save(categoria);
    }

    public void deletar(Long id) {

        Categoria categoria = buscarPorId(id);

        validarCategoriaProtegida(categoria);

        if (!categoria.getSubcategorias().isEmpty()) {
            throw new BusinessException("Categoria possui subcategorias vinculadas.");
        }

        try {
            repository.delete(categoria);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Categoria possui produtos vinculados.");
        }
    }

    private Categoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada"));
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("Nome da categoria é obrigatório");
        }
    }

    private void validarCategoriaProtegida(Categoria categoria) {
        if (CATEGORIAS_PROTEGIDAS.contains(categoria.getNomeCategoria())) {
            throw new BusinessException("Essa categoria é fundamental e não pode ser alterada");
        }
    }
}