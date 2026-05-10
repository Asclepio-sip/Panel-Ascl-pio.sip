package projeto_base_de_telas_e_login.controllers.TelaLoja;

import org.springframework.web.bind.annotation.*;

import projeto_base_de_telas_e_login.dto.loja.lojabairros.CreateLojaBairroRequest;
import projeto_base_de_telas_e_login.dto.loja.lojabairros.LojaBairroResponse;

import projeto_base_de_telas_e_login.service.Loja.LojaBairro.LojaBairroService;

import java.util.List;

@RestController
@RequestMapping("/loja-bairros")
public class LojaBairroController {

    private final LojaBairroService service;

    public LojaBairroController(
            LojaBairroService service
    ) {
        this.service = service;
    }

    @PostMapping
    public LojaBairroResponse criar(
            @RequestBody CreateLojaBairroRequest request
    ) {

        return service.criar(request);
    }

    @GetMapping("/loja/{lojaId}")
    public List<LojaBairroResponse> listarPorLoja(
            @PathVariable Long lojaId
    ) {

        return service.listarPorLoja(lojaId);
    }

    @GetMapping
    public List<LojaBairroResponse> listarTodos() {

        return service.listarTodos();
    }

    @DeleteMapping("/{id}")
    public void deletar(
            @PathVariable Long id
    ) {

        service.deletar(id);
    }
}