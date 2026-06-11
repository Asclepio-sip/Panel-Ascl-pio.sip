package projeto_base_de_telas_e_login.Loja.Loja.Controller.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Loja.Loja.LojaService;
import projeto_base_de_telas_e_login.Loja.Loja.dto.CreateLojaRequest;
import projeto_base_de_telas_e_login.Loja.Loja.dto.LojaResponse;

import java.util.List;

@RequestMapping("/lojas")
@Tag(name = "lojas", description = "Gerenciamento de lojas")
public interface LojaApi {


    @PostMapping
    @PreAuthorize("hasAuthority('LOJA_CREATE')")
    public ResponseEntity<LojaResponse> criar(@RequestBody CreateLojaRequest request);

    @GetMapping
    @PreAuthorize("hasAuthority('LOJA_READ')")
    public ResponseEntity<List<LojaResponse>> listar();

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LOJA_UPDATE')")
    public ResponseEntity<LojaResponse> atualizar(@PathVariable Long id, @RequestBody CreateLojaRequest request);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LOJA_DELETE')")
    public ResponseEntity<Void> deletar(@PathVariable Long id);
}
