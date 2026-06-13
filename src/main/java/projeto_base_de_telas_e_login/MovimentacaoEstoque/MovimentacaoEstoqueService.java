package projeto_base_de_telas_e_login.MovimentacaoEstoque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.Repository.MovimentacaoEstoqueSpecification;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.dto.MovimentacaoEstoqueResponse;

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