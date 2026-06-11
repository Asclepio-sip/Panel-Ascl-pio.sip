package projeto_base_de_telas_e_login.Loja.LojaBairro.Controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Loja.LojaBairro.dto.CreateLojaBairroRequest;
import projeto_base_de_telas_e_login.Loja.LojaBairro.dto.LojaBairroResponse;
import projeto_base_de_telas_e_login.Loja.LojaBairro.dto.LojaComBairrosResponse;

import java.util.List;

@RequestMapping("/loja-bairros")
@Tag(name = "lojasbairro", description = "Gerenciamento de lojasbairro")
public interface LojaBairroApi {


    @PostMapping
    @PreAuthorize("hasAuthority('LOJA_BAIRRO_CREATE')")
    public LojaBairroResponse criar(@RequestBody CreateLojaBairroRequest request);

    @GetMapping("/loja/{lojaId}")
    @PreAuthorize("hasAuthority('LOJA_BAIRRO_READ')")
    public List<LojaBairroResponse> listarPorLoja(@PathVariable Long lojaId);

    @GetMapping
    @PreAuthorize("hasAuthority('LOJA_BAIRRO_READ')")
    public List<LojaBairroResponse> listarTodos();

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LOJA_BAIRRO_DELETE')")
    public void deletar(@PathVariable Long id);

    @GetMapping("/lojas-com-bairros")
    @PreAuthorize("hasAuthority('LOJA_BAIRRO_READ')")
    public List<LojaComBairrosResponse> listarLojasComBairros();

}
