package projeto_base_de_telas_e_login.adapter.out.persistence.categoria;

import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.domain.model.categoria.Categoria;
import projeto_base_de_telas_e_login.domain.repository.CategoriaPorta;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaAdapter implements CategoriaPorta {

    private final CategoriaRepository repository;

    public CategoriaAdapter(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return repository.findById(id)
                .map(CategoriaEntity::toDomain);
    }

    @Override
    public Optional<Categoria> findByNome(String nome) { // 🔥 IMPLEMENTAR
        return repository.findByNomeCategoria(nome)
                .map(CategoriaEntity::toDomain);
    }

    @Override
    public List<Categoria> findAll() {
        return repository.findAll()
                .stream()
                .map(CategoriaEntity::toDomain)
                .toList();
    }

    @Override
    public Categoria save(Categoria categoria) {
        repository.save(new CategoriaEntity(categoria));
        return categoria;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
