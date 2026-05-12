package projeto_base_de_telas_e_login.Estoque.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import projeto_base_de_telas_e_login.Estoque.controller.api.EstoqueApi;
import projeto_base_de_telas_e_login.Estoque.dto.*;
import projeto_base_de_telas_e_login.Estoque.EstoqueService;

import java.util.List;

@RestController
public class TelaEstoque implements EstoqueApi {

    private final EstoqueService service;

    public TelaEstoque(EstoqueService useCase) {
        this.service = useCase;
    }

    @Override
    public ResponseEntity<Void> criar(EstoqueAddDto dto) {

        service.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> atualizar(EstoqueAddDto dto) {

        service.atualizar(dto.lojaID(), dto.produtoId(), dto.quantidade(), dto.precoVenda());

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> aplicarPromocao(AplicarPromocaoDto dto) {

        service.aplicarPromocao(dto.lojaId(), dto.produtoId(), dto.percentual());

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ListaDeEstoqueDasLojasResponse>> completo() {

        List<ListaDeEstoqueDasLojasResponse> response = service.lista().stream().map(ListaDeEstoqueDasLojasResponse::fromDomain).toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EstoqueCardDto>> relatorio() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @Override
    public ResponseEntity<List<EstoqueListaDto>> buscarPorNomeLoja(String nome) {

        List<EstoqueListaDto> response = service.buscarPorNomeLoja(nome).stream().map(EstoqueListaDto::fromDomain).toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EstoqueListaDto>> buscarPorNomeProduto(String nome) {

        List<EstoqueListaDto> response = service.buscarPorNomeProduto(nome).stream().map(EstoqueListaDto::fromDomain).toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EstoqueListaDto>> filtrar(Long lojaId, String nomeLoja, Boolean semEstoque) {

        List<EstoqueListaDto> response = service.filtrar(lojaId, nomeLoja, semEstoque).stream().map(EstoqueListaDto::fromDomain).toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}