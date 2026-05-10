package projeto_base_de_telas_e_login.controllers.TelaEstoque;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import projeto_base_de_telas_e_login.controllers.TelaEstoque.api.EstoqueApi;
import projeto_base_de_telas_e_login.dto.Estoque.AplicarPromocaoDto;
import projeto_base_de_telas_e_login.dto.Estoque.EstoqueAddDto;
import projeto_base_de_telas_e_login.dto.Estoque.EstoqueListaDto;
import projeto_base_de_telas_e_login.service.Estoque.EstoqueService;

import java.util.List;

@RestController
public class TelaEstoque implements EstoqueApi {

    private final EstoqueService useCase;

    public TelaEstoque(EstoqueService useCase) {
        this.useCase = useCase;
    }

    @Override
    public ResponseEntity<Void> criar(EstoqueAddDto dto) {

        useCase.criar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> atualizar(EstoqueAddDto dto) {

        useCase.atualizar(
                dto.lojaID(),
                dto.produtoId(),
                dto.quantidade(),
                dto.precoVenda()
        );

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> aplicarPromocao(AplicarPromocaoDto dto) {

        useCase.aplicarPromocao(
                dto.lojaId(),
                dto.produtoId(),
                dto.percentual()
        );

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<EstoqueListaDto>> listar() {

        List<EstoqueListaDto> response = useCase
                .listarTodos()
                .stream()
                .map(EstoqueListaDto::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EstoqueListaDto>> buscarPorNomeLoja(String nome) {

        List<EstoqueListaDto> response = useCase
                .buscarPorNomeLoja(nome)
                .stream()
                .map(EstoqueListaDto::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EstoqueListaDto>> buscarPorNomeProduto(String nome) {

        List<EstoqueListaDto> response = useCase
                .buscarPorNomeProduto(nome)
                .stream()
                .map(EstoqueListaDto::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EstoqueListaDto>> filtrar(
            Long lojaId,
            String nomeLoja,
            Boolean semEstoque
    ) {

        List<EstoqueListaDto> response = useCase
                .filtrar(lojaId, nomeLoja, semEstoque)
                .stream()
                .map(EstoqueListaDto::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {

        useCase.deletar(id);

        return ResponseEntity.noContent().build();
    }
}