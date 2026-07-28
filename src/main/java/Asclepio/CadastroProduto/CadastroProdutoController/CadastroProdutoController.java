package Asclepio.CadastroProduto.CadastroProdutoController;


import Asclepio.CadastroProduto.CadastroProdutoController.api.CadastroProdutoApi;
import Asclepio.CadastroProduto.CadastroProdutoService.CadastroProdutoService;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroProdutoController implements CadastroProdutoApi {

    private final CadastroProdutoService cadastroProdutoService;

    public CadastroProdutoController(CadastroProdutoService cadastroProdutoService) {
        this.cadastroProdutoService = cadastroProdutoService;
    }

    @Override
    public ResponseEntity<CadastroProdutoCompletoResponse> cadastrar(
            CadastroProdutoCompletoDTO dto
    ) {

        CadastroProdutoCompletoResponse response =
                cadastroProdutoService.cadastrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}