package projeto_base_de_telas_e_login.service.Categoria;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.model.categoria.Categoria;
import projeto_base_de_telas_e_login.repository.CategoriaPorta;

import java.util.List;
import java.util.Set;

@Service
public class CategoriaUseCase {

    private final CategoriaPorta categoriaPorta;

    private static final Set<String> CATEGORIAS_PROTEGIDAS = Set.of("Medicamentos","Beleza","Higiene","Infantil","Vitaminas","Promoções");

    public CategoriaUseCase(CategoriaPorta categoriaPorta) {
        this.categoriaPorta = categoriaPorta;
    }

    // 🔹 LISTAR
    public List<Categoria> listarTodas() {
        return categoriaPorta.findAll();
    }

    public Categoria criar(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório");
        }

        categoriaPorta.findByNome(nome)
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Categoria já existe");
                });

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(nome);  // ✅ ajustado

        return categoriaPorta.save(categoria);
    }

    // 🔹 EDITAR
    public Categoria editar(Long id, String novoNome) {

        Categoria categoria = categoriaPorta.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (CATEGORIAS_PROTEGIDAS.contains(categoria.getNomeCategoria())) {  // ✅ ajustado
            throw new IllegalStateException(
                    "Essa categoria é fundamental e não pode ser editada"
            );
        }

        categoria.setNomeCategoria(novoNome);  // ✅ ajustado
        return categoriaPorta.save(categoria);
    }

    // 🔹 DELETAR
    public void deletar(Long id) {

        Categoria categoria = categoriaPorta.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (CATEGORIAS_PROTEGIDAS.contains(categoria.getNomeCategoria())) {  // ✅ ajustado
            throw new IllegalStateException(
                    "Essa categoria é fundamental e não pode ser deletada"
            );
        }

        try {
            categoriaPorta.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Categoria possui produtos vinculados.");
        }
    }
}