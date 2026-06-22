package Asclepio.Empresa.controller;

import Asclepio.Empresa.EmpresaService;
import Asclepio.Empresa.controller.api.EmpresaApi;
import Asclepio.Empresa.dto.EmpresaFiltroDTO;
import Asclepio.Empresa.dto.EmpresaRequest;
import Asclepio.Empresa.dto.EmpresaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpresaController implements EmpresaApi {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Page<EmpresaResponse>> listar(EmpresaFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<EmpresaResponse> buscarPorId(Long id) {
        return ResponseEntity.ok(service.buscarResponsePorId(id));
    }

    @Override
    public ResponseEntity<EmpresaResponse> criar(EmpresaRequest request) {
        return ResponseEntity.ok(service.criar(request));
    }

    @Override
    public ResponseEntity<EmpresaResponse> editar(Long id, EmpresaRequest request) {
        return ResponseEntity.ok(service.editar(id, request));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}