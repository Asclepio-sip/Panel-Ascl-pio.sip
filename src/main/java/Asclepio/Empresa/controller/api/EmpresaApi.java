//package Asclepio.Empresa.controller.api;
//
//import Asclepio.Empresa.dto.EmpresaFiltroDTO;
//import Asclepio.Empresa.dto.EmpresaRequest;
//import Asclepio.Empresa.dto.EmpresaResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springdoc.core.annotations.ParameterObject;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RequestMapping("/empresas")
//@Tag(name = "Empresas")
//public interface EmpresaApi {
//
//    @GetMapping
//    @PreAuthorize("hasAuthority('VerEmpresa')")
//    @Operation(summary = "Listar empresas")
//    ResponseEntity<Page<EmpresaResponse>> listar(
//            @ParameterObject EmpresaFiltroDTO filtro,
//            @ParameterObject Pageable pageable
//    );
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VerEmpresa')")
//    @Operation(summary = "Buscar empresa por ID")
//    ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id);
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('CriarEmpresa')")
//    @Operation(summary = "Criar empresa")
//    ResponseEntity<EmpresaResponse> criar(@RequestBody EmpresaRequest request);
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('EditarEmpresa')")
//    @Operation(summary = "Editar empresa")
//    ResponseEntity<EmpresaResponse> editar(
//            @PathVariable Long id,
//            @RequestBody EmpresaRequest request
//    );
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ExcluirEmpresa')")
//    @Operation(summary = "Deletar empresa")
//    ResponseEntity<Void> deletar(@PathVariable Long id);
//}