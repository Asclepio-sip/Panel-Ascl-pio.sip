package projeto_base_de_telas_e_login.controllers.TelaLoja;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.dto.loja.Bairro.BairroRequestDTO;
import projeto_base_de_telas_e_login.dto.loja.Bairro.BairroResponseDTO;
import projeto_base_de_telas_e_login.service.Loja.Bairro.AdicionarBairroUseCase;
import projeto_base_de_telas_e_login.model.Loja.Bairro;

import java.util.List;

@RestController
@RequestMapping("/bairros")
public class BairroController {

    private final AdicionarBairroUseCase bairroUseCase;

    public BairroController(AdicionarBairroUseCase bairroUseCase) {
        this.bairroUseCase = bairroUseCase;
    }

    @GetMapping
    public ResponseEntity<List<BairroResponseDTO>> listar() {
        List<Bairro> bairros = bairroUseCase.listar();
        List<BairroResponseDTO> dtoList = bairros.stream()
                .map(BairroResponseDTO::fromDomain) // ✅ aqui é o fromDomain
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping
    public ResponseEntity<BairroResponseDTO> criar(@RequestBody BairroRequestDTO request) {
        Bairro bairro = bairroUseCase.executar(request.nome());
        return ResponseEntity.ok(BairroResponseDTO.fromDomain(bairro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BairroResponseDTO> atualizar(@PathVariable Long id,
                                                       @RequestBody BairroRequestDTO request) {
        Bairro bairro = bairroUseCase.atualizar(id, request.nome());
        return ResponseEntity.ok(BairroResponseDTO.fromDomain(bairro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bairroUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}