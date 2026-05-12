package projeto_base_de_telas_e_login.Produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_base_de_telas_e_login.Produto.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoria_NomeCategoria(String nomeCategoria);

    Optional<Product> findByName(String name);


}