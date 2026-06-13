package projeto_base_de_telas_e_login.MovimentacaoEstoque.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.Controller.Api.MovimentacaoEstoqueApi;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.MovimentacaoEstoqueService;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.dto.MovimentacaoEstoqueResponse;

@RestController
public class MovimentacaoEstoqueController implements MovimentacaoEstoqueApi {

    private final MovimentacaoEstoqueService service;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Page<MovimentacaoEstoqueResponse>> listar(
            MovimentacaoEstoqueFiltro filtro,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }
}