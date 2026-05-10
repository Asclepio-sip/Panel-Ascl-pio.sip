package projeto_base_de_telas_e_login.controllers.TelaLoja;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.dto.loja.Bairro.BairroRequestDTO;
import projeto_base_de_telas_e_login.dto.loja.Bairro.BairroResponseDTO;
import projeto_base_de_telas_e_login.persistence.Loja.Bairro.Bairro;
import projeto_base_de_telas_e_login.service.Loja.Bairro.BairroService;

import java.util.List;
@RestController
@RequestMapping("/bairro")
public class BairroController {

    private final BairroService service;

    public BairroController(BairroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BairroResponseDTO> criar(
            @RequestBody BairroRequestDTO dto
    ) {

        Bairro bairro = service.criar(dto.nome());

        return ResponseEntity.ok(
                BairroResponseDTO.fromEntity(bairro)
        );
    }

    @GetMapping
    public ResponseEntity<List<BairroResponseDTO>> listar() {

        List<BairroResponseDTO> response =
                service.listar()
                        .stream()
                        .map(BairroResponseDTO::fromEntity)
                        .toList();

        return ResponseEntity.ok(response);
    }
}