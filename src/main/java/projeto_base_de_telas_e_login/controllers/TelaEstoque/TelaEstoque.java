package projeto_base_de_telas_e_login.controllers.TelaEstoque;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.dto.Estoque.EstoqueAddDto;
import projeto_base_de_telas_e_login.service.Estoque.EstoqueUseCase;

import java.math.BigDecimal;


@RestController
@RequestMapping("/estoque")
public class TelaEstoque {

    private final EstoqueUseCase useCase;

    public TelaEstoque(EstoqueUseCase useCase) {
        this.useCase = useCase;
    }

    // 🔥 POST - criar
    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody EstoqueAddDto dto) {
        useCase.criar(dto);
        return ResponseEntity.status(201).build();
    }

    // 🔥 PUT - atualizar
    @PutMapping
    public ResponseEntity<Void> atualizar(@RequestBody EstoqueAddDto dto) {
        useCase.atualizar(dto.lojaID(),dto.produtoId(),dto.quantidade(),dto.precoVenda());
        return ResponseEntity.ok().build();
    }

    // 🔥 PATCH - promoção Paracetamol  Loja Entrega Express
    @PatchMapping("/promocao")
    public ResponseEntity<Void> aplicarPromocao(
            @RequestParam Long lojaId,
            @RequestParam Long produtoId,
            @RequestParam BigDecimal percentual
    ) {

        useCase.aplicarPromocao(lojaId, produtoId, percentual);

        return ResponseEntity.ok().build();
    }

    // 🔎 GET - listar todos
    @GetMapping
    public ResponseEntity<?> listar()  {
        return ResponseEntity.ok(useCase.listarTodos());
    }

    // 🔎 GET - buscar por nome da loja
    @GetMapping("/loja")
    public ResponseEntity<?> buscarPorNomeLoja(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(
                useCase.buscarPorNomeLoja(nome)
        );
    }

    // 🔎 GET - buscar por nome do produto
    @GetMapping("/produto")
    public ResponseEntity<?> buscarPorNomeProduto(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(
                useCase.buscarPorNomeProduto(nome)
        );
    }

    @GetMapping("/filtro")
    public ResponseEntity<?> filtrar(
            @RequestParam(required = false) Long lojaId,
            @RequestParam(required = false) String nomeLoja,
            @RequestParam(required = false) Boolean semEstoque
    ) {

        return ResponseEntity.ok(
                useCase.filtrar(lojaId, nomeLoja, semEstoque)
        );
    }

}