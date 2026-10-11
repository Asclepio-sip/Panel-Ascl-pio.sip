package Asclepio.Plano.Controller;

import Asclepio.Plano.Controller.api.PlanoApi;
import Asclepio.Plano.PlanoService;
import Asclepio.Plano.dto.PlanoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanoController implements PlanoApi {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @Override
    public ResponseEntity<List<PlanoResponseDTO>> listar() {
        return ResponseEntity.ok(planoService.listar());
    }
}
