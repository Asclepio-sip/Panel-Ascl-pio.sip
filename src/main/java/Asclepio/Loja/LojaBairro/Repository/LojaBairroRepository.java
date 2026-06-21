package Asclepio.Loja.LojaBairro.Repository;

import Asclepio.Loja.LojaBairro.LojaBairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LojaBairroRepository extends JpaRepository<LojaBairro, Long>, JpaSpecificationExecutor<LojaBairro> {

    boolean existsByLoja_IdAndBairro_Id(Long lojaId, Long bairroId);

    Optional<LojaBairro> findByIdAndLoja_Empresa_Id(Long id, Long empresaId);

    boolean existsByLoja_IdAndBairro_IdAndLoja_Empresa_Id(
            Long lojaId,
            Long bairroId,
            Long empresaId
    );

    Optional<LojaBairro> findByLoja_IdAndBairro_Id(Long lojaId, Long bairroId);


}