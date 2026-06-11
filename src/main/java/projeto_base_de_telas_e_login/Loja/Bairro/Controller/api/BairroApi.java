package projeto_base_de_telas_e_login.Loja.Bairro.Controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Loja.Bairro.Bairro;
import projeto_base_de_telas_e_login.Loja.Bairro.dto.BairroRequestDTO;
import projeto_base_de_telas_e_login.Loja.Bairro.dto.BairroResponseDTO;

import java.util.List;

@RequestMapping("/bairro")
@Tag(name = "bairro", description = "Gerenciamento de bairro")
public interface BairroApi {


    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORIA_CREATE')")
    public ResponseEntity<BairroResponseDTO> criar(@RequestBody BairroRequestDTO dto);

    @GetMapping
    @PreAuthorize("hasAuthority('CATEGORIA_CREATE')")
    public ResponseEntity<List<BairroResponseDTO>> listar();


    @DeleteMapping
    @PreAuthorize("hasAuthority('CATEGORIA_CREATE')")
    public ResponseEntity  deletarBairro(@PathVariable Long id);


}
