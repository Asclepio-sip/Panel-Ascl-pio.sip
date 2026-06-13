package projeto_base_de_telas_e_login.Produto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import projeto_base_de_telas_e_login.Produto.Product;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findByName(String name);
}