package Asclepio.Plano.Controller;

import Asclepio.Plano.Controller.api.PlanoApi;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Plano.LimitePlanoService;
import Asclepio.Plano.PlanoService;
import Asclepio.Plano.dto.PlanoEmpresaResponseDTO;
import Asclepio.Plano.dto.PlanoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanoController implements PlanoApi {

    private final PlanoService planoService;
    private final LimitePlanoService limitePlanoService;
    private final EmpresaContext empresaContext;

    public PlanoController(PlanoService planoService, LimitePlanoService limitePlanoService,
                           EmpresaContext empresaContext) {
        this.planoService = planoService;
        this.limitePlanoService = limitePlanoService;
        this.empresaContext = empresaContext;
    }

    @Override
    public ResponseEntity<List<PlanoResponseDTO>> listar() {
        return ResponseEntity.ok(planoService.listar());
    }

    @Override
    public ResponseEntity<PlanoEmpresaResponseDTO> atual() {
        return ResponseEntity.ok(limitePlanoService.resumo(empresaContext.getEmpresaId()));
    }
}
