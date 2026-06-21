package Asclepio.Loja.Loja.Repository;

import Asclepio.Loja.Loja.Loja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LojaRepository extends JpaRepository<Loja, Long>, JpaSpecificationExecutor<Loja> {

    Optional<Loja> findByNomeLoja(String nomeLoja);

    Optional<Loja> findByIdAndEmpresa_Id(Long id, Long empresaId);

    Optional<Loja> findByNomeLojaIgnoreCaseAndEmpresa_Id(String nomeLoja, Long empresaId);

    Optional<Loja> findByCnpjAndEmpresa_Id(String cnpj, Long empresaId);

}