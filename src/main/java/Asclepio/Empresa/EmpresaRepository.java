package Asclepio.Empresa;

import Asclepio.Usuario.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {

    Optional<Empresa> findByCnpj(String cnpj);

    Optional<Empresa> findByNome(String nome);

    /** true se a empresa existe e está bloqueada (EMP_ATIVA = false). */
    boolean existsByIdAndAtivaFalse(Long id);

}