package Asclepio.Loja.Bairro.Repository;

import Asclepio.Loja.Bairro.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BairroRepository extends JpaRepository<Bairro, Long>, JpaSpecificationExecutor<Bairro> {

    Optional<Bairro> findByIdAndEmpresa_Id(Long id, Long empresaId);

    Optional<Bairro> findByNomeIgnoreCaseAndEmpresa_Id(String nome, Long empresaId);

    boolean existsByNomeIgnoreCaseAndEmpresa_Id(String nome, Long empresaId);
}