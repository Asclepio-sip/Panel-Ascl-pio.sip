package projeto_base_de_telas_e_login.Estoque.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(EstoqueFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(filtro, pageable));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}