package Asclepio.MovimentacaoEstoque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueSpecification;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueResponse;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository repository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository repository) {
        this.repository = repository;
    }

    public Page<MovimentacaoEstoqueResponse> listar(
            MovimentacaoEstoqueFiltro filtro,
            Pageable pageable
    ) {
        return repository
                .findAll(MovimentacaoEstoqueSpecification.filtrar(filtro), pageable)
                .map(MovimentacaoEstoqueResponse::fromEntity);
    }
}