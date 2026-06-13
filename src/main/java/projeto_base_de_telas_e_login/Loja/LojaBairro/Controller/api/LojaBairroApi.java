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
    @PreAuthorize("hasAuthority('CriarLojaBairro')")
    public LojaBairroResponse criar(@RequestBody CreateLojaBairroRequest request);

    @GetMapping("/loja/{lojaId}")
    @PreAuthorize("hasAuthority('VerLojaBairro')")
    public List<LojaBairroResponse> listarPorLoja(@PathVariable Long lojaId);

    @GetMapping
    @PreAuthorize("hasAuthority('VerLojaBairro')")
    public List<LojaBairroResponse> listarTodos();

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirLojaBairro')")
    public void deletar(@PathVariable Long id);

    @GetMapping("/lojas-com-bairros")
    @PreAuthorize("hasAuthority('VerLojaBairro')")
    public List<LojaComBairrosResponse> listarLojasComBairros();

}
