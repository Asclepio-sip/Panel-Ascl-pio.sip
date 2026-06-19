package Asclepio.Loja.LojaBairro.Repository;

import Asclepio.Loja.LojaBairro.LojaBairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LojaBairroRepository extends JpaRepository<LojaBairro, Long>, JpaSpecificationExecutor<LojaBairro> {

    List<LojaBairro> findByLoja_Id(Long lojaId);

    boolean existsByLoja_IdAndBairro_Id(Long lojaId, Long bairroId);

    Optional<LojaBairro> findByLoja_IdAndBairro_Id(Long lojaId, Long bairroId);
}