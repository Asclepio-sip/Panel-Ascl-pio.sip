package projeto_base_de_telas_e_login.domain.repository;

import projeto_base_de_telas_e_login.domain.model.categoria.Categoria;

import java.util.Optional;
import java.util.List;

public interface CategoriaPorta {

    Categoria save(Categoria categoria);

    Optional<Categoria> findById(Long id);

    Optional<Categoria> findByNome(String nome); // 🔥 ADICIONAR

    List<Categoria> findAll();

    void deleteById(Long id); // 🔥 adicionar

}
