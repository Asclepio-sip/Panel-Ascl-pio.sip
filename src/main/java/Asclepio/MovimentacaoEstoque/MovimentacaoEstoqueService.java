package Asclepio.MovimentacaoEstoque;

import Asclepio.Empresa.EmpresaContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueSpecification;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueResponse;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoUsuarioResponse;

import java.util.Comparator;
import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository repository;
    private final EmpresaContext empresaContext;

    public MovimentacaoEstoqueService(
            MovimentacaoEstoqueRepository repository,
            EmpresaContext empresaContext
    ) {
        this.repository = repository;
        this.empresaContext = empresaContext;
    }

    public List<MovimentacaoUsuarioResponse> listarUsuarios() {
        return repository.usuariosComMovimentacao(empresaContext.getEmpresaId())
                .stream()
                .sorted(Comparator.comparing(MovimentacaoUsuarioResponse::nome, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Page<MovimentacaoEstoqueResponse> listar(
            MovimentacaoEstoqueFiltro filtro,
            Pageable pageable
    ) {
        return repository.findAll(
                MovimentacaoEstoqueSpecification.filtrar(
                        filtro,
                        empresaContext.getEmpresaId()
                ),
                pageable
        ).map(MovimentacaoEstoqueResponse::fromEntity);
    }
}