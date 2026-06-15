package Asclepio.Loja.Bairro.Controller;

import Asclepio.Loja.Bairro.Bairro;
import Asclepio.Loja.Bairro.BairroService;
import Asclepio.Loja.Bairro.Controller.api.BairroApi;
import Asclepio.Loja.Bairro.dto.BairroFiltroDTO;
import Asclepio.Loja.Bairro.dto.BairroRequestDTO;
import Asclepio.Loja.Bairro.dto.BairroResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BairroController implements BairroApi {

    private final BairroService service;

    public BairroController(BairroService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<BairroResponseDTO> criar(BairroRequestDTO dto) {

        Bairro bairro = service.criar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BairroResponseDTO.fromEntity(bairro));
    }

    @Override
    public ResponseEntity<Page<BairroResponseDTO>> listar(BairroFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<BairroResponseDTO> atualizar(Long id, BairroRequestDTO dto) {
        return ResponseEntity.ok(
                BairroResponseDTO.fromEntity(
                        service.atualizar(id, dto)
                )
        );
    }

    @Override
    public ResponseEntity<Void> deletarBairro(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}