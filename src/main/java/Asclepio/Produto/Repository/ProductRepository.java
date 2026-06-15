package Asclepio.Produto.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import Asclepio.Produto.Product;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findByName(String name);
}