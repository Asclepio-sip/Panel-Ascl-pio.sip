package projeto_base_de_telas_e_login.service.Categoria;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.persistence.categoria.Categoria;
import projeto_base_de_telas_e_login.persistence.categoria.CategoriaRepository;

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

    // LISTAR
    public List<Categoria> listarTodas() {
        return repository.findAll();
    }

    // CRIAR
    public Categoria criar(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "Nome da categoria é obrigatório"
            );
        }

        repository.findByNomeCategoria(nome)
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "Categoria já existe"
                    );
                });

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(nome);

        return repository.save(categoria);
    }

    // EDITAR
    public Categoria editar(Long id, String novoNome) {

        Categoria categoria = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada")
                );

        if (CATEGORIAS_PROTEGIDAS.contains(
                categoria.getNomeCategoria()
        )) {

            throw new IllegalStateException(
                    "Essa categoria é fundamental e não pode ser editada"
            );
        }

        categoria.setNomeCategoria(novoNome);

        return repository.save(categoria);
    }

    // DELETAR
    public void deletar(Long id) {

        Categoria categoria = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada")
                );

        if (CATEGORIAS_PROTEGIDAS.contains(
                categoria.getNomeCategoria()
        )) {

            throw new IllegalStateException(
                    "Essa categoria é fundamental e não pode ser deletada"
            );
        }

        try {
            repository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new IllegalStateException(
                    "Categoria possui produtos vinculados."
            );
        }
    }
}