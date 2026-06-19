package Asclepio.Loja.Loja.Repository;

import Asclepio.Loja.Loja.Loja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LojaRepository extends JpaRepository<Loja, Long>, JpaSpecificationExecutor<Loja> {

    Optional<Loja> findByNomeLoja(String nomeLoja);

    Optional<Loja> findByNomeLojaIgnoreCase(String nomeLoja);

    Optional<Loja> findByCnpj(String cnpj);

    boolean existsByNomeLojaIgnoreCase(String nomeLoja);

    boolean existsByCnpj(String cnpj);
}