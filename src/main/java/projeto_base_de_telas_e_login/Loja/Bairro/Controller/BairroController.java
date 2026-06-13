package projeto_base_de_telas_e_login.Loja.Bairro.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Loja.Bairro.Bairro;
import projeto_base_de_telas_e_login.Loja.Bairro.BairroService;
import projeto_base_de_telas_e_login.Loja.Bairro.Controller.api.BairroApi;
import projeto_base_de_telas_e_login.Loja.Bairro.dto.BairroRequestDTO;
import projeto_base_de_telas_e_login.Loja.Bairro.dto.BairroResponseDTO;

import java.util.List;
@RestController
public class BairroController implements BairroApi {

    private final BairroService service;

    public BairroController(BairroService service) {
        this.service = service;
    }

    public ResponseEntity<BairroResponseDTO> criar(@RequestBody BairroRequestDTO dto) {

        Bairro bairro = service.criar(dto.nome());

        return ResponseEntity.ok(BairroResponseDTO.fromEntity(bairro));
    }

    public ResponseEntity<List<BairroResponseDTO>> listar() {

        List<BairroResponseDTO> response = service.listar().stream().map(BairroResponseDTO::fromEntity).toList();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity  deletarBairro(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}