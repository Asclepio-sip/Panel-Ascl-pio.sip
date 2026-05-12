package projeto_base_de_telas_e_login.Categoria.Service.imple;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.Categoria.Categoria;
import projeto_base_de_telas_e_login.Categoria.Repository.CategoriaRepository;

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

    public Categoria criar(String nome) {

        validarNome(nome);

        repository.findByNomeCategoria(nome)
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Categoria já existe");
                });

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(nome.trim());

        return repository.save(categoria);
    }

    public Categoria editar(Long id, String novoNome) {

        validarNome(novoNome);

        Categoria categoria = buscarPorId(id);

        validarCategoriaProtegida(categoria);

        categoria.setNomeCategoria(novoNome.trim());

        return repository.save(categoria);
    }

    public void deletar(Long id) {

        Categoria categoria = buscarPorId(id);

        validarCategoriaProtegida(categoria);

        try {

            repository.delete(categoria);

        } catch (DataIntegrityViolationException e) {

            throw new IllegalStateException(
                    "Categoria possui produtos vinculados."
            );
        }
    }

    private Categoria buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada"));
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException(
                    "Nome da categoria é obrigatório"
            );
        }
    }

    private void validarCategoriaProtegida(Categoria categoria) {

        if (CATEGORIAS_PROTEGIDAS.contains(categoria.getNomeCategoria())) {

            throw new IllegalStateException(
                    "Essa categoria é fundamental e não pode ser alterada"
            );
        }
    }
}