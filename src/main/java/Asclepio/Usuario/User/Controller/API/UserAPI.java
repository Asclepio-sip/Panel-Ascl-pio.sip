package Asclepio.Usuario.User.Controller.API;


import Asclepio.Usuario.User.dto.*;
import Asclepio.config.security.UsuarioAutenticado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;


@RequestMapping("/user")
@Tag(name = "Usuários", description = "Gerenciamento de usuários e autenticação")
public interface UserAPI {

    @Operation(summary = "Login", description = """
            Autentica o usuário.
            
            Comportamento:
            
            • Se o usuário possuir acesso a apenas uma loja, o token JWT é retornado imediatamente.
            
            • Se possuir acesso a mais de uma loja, será retornada a lista de lojas disponíveis para que o cliente escolha em qual deseja entrar.
            """)
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data);


    @Operation(summary = "Selecionar loja", description = """
            Seleciona a loja que será utilizada na sessão.
            
            Este endpoint deve ser chamado apenas quando o login retornar
            que o usuário possui acesso a mais de uma loja.
            
            Após selecionar a loja é gerado o JWT definitivo contendo:
            
            • empresaId
            • lojaId
            • permissões da role naquela loja
            """)
    @PostMapping("/escolher-loja")
    ResponseEntity<LoginResponseDTO> escolherLoja(@RequestBody @Valid EscolherLojaDTO dto, @AuthenticationPrincipal UsuarioAutenticado usuario);

    @Operation(summary = "Cadastrar usuário")
    @PostMapping
    @PreAuthorize("hasAuthority('CriarUser')")
    ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO dto);


    @Operation(summary = "Criar empresa e usuário administrador")
    @PostMapping("/CriarConta")
    ResponseEntity<LoginResponseDTO> CriarConta(@RequestBody @Valid RequestCriarContaDTO dto);


    @Operation(summary = "Listar usuários", description = """
            Lista os usuários da empresa utilizando paginação e filtros.
            
            Filtros disponíveis:
            
            • login
            • ativo
            • roleId
            • nomeRole
            """)
    @GetMapping
    @PreAuthorize("hasAuthority('VerUser')")
    ResponseEntity<Page<ResponseListaDeUserDTO>> listarUsuarios(@ParameterObject UserFiltroDTO filtro, @ParameterObject Pageable pageable);


    @Operation(summary = "Atualizar usuário")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarUser')")
    ResponseEntity<Void> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO dto);
}