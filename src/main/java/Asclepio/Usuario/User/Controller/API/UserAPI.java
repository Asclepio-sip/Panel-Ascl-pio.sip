package Asclepio.Usuario.User.Controller.API;


import Asclepio.Usuario.User.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RequestMapping("/user")
@Tag(name = "user", description = "Gerenciamento de user")
public interface UserAPI {


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data);

    @PostMapping
    @PreAuthorize("hasAuthority('CriarUser')")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO dto);


    @PostMapping("/CriarConta")
    public ResponseEntity<LoginResponseDTO> CriarConta(@RequestBody @Valid RequestCriarContaDTO dto);

    @Operation(summary = "Listar usuários", description = """
            Lista os usuários do sistema com paginação e filtros opcionais.
            
            Filtros disponíveis:
            
            • login -> filtra pelo nome de usuário.
            • ativo -> filtra usuários ativos ou inativos.
            • roleId -> filtra pelo ID do cargo (UUID).
            • nomeRole -> filtra pelo nome do cargo.
            
            Exemplos:
            
            Listar todos:
            GET /user?page=0&size=10
            
            Buscar por login:
            GET /user?login=mateus&page=0&size=10
            
            Buscar apenas usuários ativos:
            GET /user?ativo=true&page=0&size=10
            
            Buscar usuários com cargo ADMIN:
            GET /user?nomeRole=ADMIN&page=0&size=10
            
            Buscar por ID do cargo:
            GET /user?roleId=3fa85f64-5717-4562-b3fc-2c963f66afa6&page=0&size=10
            
            Combinar filtros:
            GET /user?login=mat&ativo=true&nomeRole=ADMIN&page=0&size=10
            
            Ordenar:
            GET /user?page=0&size=10&sort=username,asc
            GET /user?page=0&size=10&sort=username,desc
            """)
    @GetMapping
    @PreAuthorize("hasAuthority('VerUser')")
    ResponseEntity<Page<ResponseListaDeUserDTO>> listarUsuarios(@ParameterObject UserFiltroDTO filtro, @ParameterObject Pageable pageable);

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('EditarUser')")
    public ResponseEntity<Void> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO dto);
}
