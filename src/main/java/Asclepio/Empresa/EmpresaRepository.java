package Asclepio.Empresa;

import Asclepio.Plano.Plano;
import Asclepio.Usuario.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {

    Optional<Empresa> findByCnpj(String cnpj);

    Optional<Empresa> findByNome(String nome);

    /** true se a empresa existe e está bloqueada (EMP_ATIVA = false). */
    boolean existsByIdAndAtivaFalse(Long id);

    /** Plano excluído no SaaS: as empresas que estavam nele ficam sem plano (sem limites). */
    @Modifying
    @Query("update Empresa e set e.plano = null where e.plano = :plano")
    int removerPlano(Plano plano);

}