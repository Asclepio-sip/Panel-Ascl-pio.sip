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

    @Operation(
            summary = "Login",
            description = "Autentica o usuário. "
                    + "Se o usuário possuir acesso a apenas uma loja, o token JWT completo é retornado imediatamente. "
                    + "Se possuir acesso a mais de uma loja, é retornado um token temporario (valido por 5 minutos, "
                    + "utilizavel apenas no endpoint /user/escolher-loja) junto com a lista de lojas disponiveis "
                    + "para que o cliente escolha em qual deseja entrar."
    )
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data);

    @Operation(
            summary = "Selecionar loja",
            description = "Seleciona a loja que sera utilizada na sessao. "
                    + "Este endpoint deve ser chamado apenas quando o login retornar que o usuario possui acesso a mais de uma loja. "
                    + "Requer o token temporario retornado pelo /login (header Authorization: Bearer {tempToken}). "
                    + "Apos selecionar a loja e gerado o JWT definitivo contendo empresaId, lojaId e as permissoes da role naquela loja."
    )
    @PostMapping("/escolher-loja")
    ResponseEntity<LoginResponseDTO> escolherLoja(@RequestBody @Valid EscolherLojaDTO dto, @AuthenticationPrincipal UsuarioAutenticado usuario);

    @Operation(
            summary = "Cadastrar funcionário",
            description = "Cria um novo usuario (funcionario) vinculado a uma ou mais lojas da empresa autenticada. "
                    + "O login e gerado automaticamente no formato: {nomeDaEmpresa}@{nomeDoUsuario} (normalizado, sem espacos, acentos ou caracteres especiais). "
                    + "Cada item da lista 'lojas' define uma loja e o cargo (role) que o usuario tera especificamente naquela loja, "
                    + "permitindo que o mesmo funcionario tenha cargos diferentes em lojas diferentes. "
                    + "Exemplo de payload: { \"nome\": \"Joao\", \"password\": \"123\", \"email\": \"joao@teste.com\", "
                    + "\"lojas\": [ { \"lojaId\": 1, \"roleId\": \"uuid-role-atendente\" }, { \"lojaId\": 2, \"roleId\": \"uuid-role-caixa\" } ] }. "
                    + "Se o funcionario for vinculado a apenas 1 loja, no login ele recebe o token completo direto. "
                    + "Se for vinculado a 2 ou mais lojas, no login ele passa pelo fluxo de selecao de loja (veja /login e /escolher-loja)."
    )
    @PostMapping
    @PreAuthorize("hasAuthority('CriarUser')")
    ResponseEntity<CriarUsuarioResponseDTO> register(@RequestBody @Valid RegisterDTO dto);

    @Operation(summary = "Criar empresa e usuário administrador")
    @PostMapping("/CriarConta")
    ResponseEntity<LoginResponseDTO> CriarConta(@RequestBody @Valid RequestCriarContaDTO dto);

    @Operation(
            summary = "Listar usuários",
            description = "Lista os usuarios da empresa utilizando paginacao e filtros. "
                    + "Filtros disponiveis: login, ativo, roleId, nomeRole."
    )
    @GetMapping
    @PreAuthorize("hasAuthority('VerUser')")
    ResponseEntity<Page<ResponseListaDeUserDTO>> listarUsuarios(@ParameterObject UserFiltroDTO filtro, @ParameterObject Pageable pageable);

    @Operation(summary = "Atualizar usuário")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarUser')")
    ResponseEntity<Void> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO dto);
}