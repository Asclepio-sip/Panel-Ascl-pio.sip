package projeto_base_de_telas_e_login.Usuario.User.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserFiltroDTO(

        @Schema(
                description = "Nome de usuário",
                example = "mateus"
        )
        String login,

        @Schema(
                description = "Status do usuário",
                example = "true"
        )
        Boolean ativo,

        @Schema(
                description = "ID do cargo do usuário",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID roleId,

        @Schema(
                description = "Nome do cargo",
                example = "ADMIN"
        )
        String nomeRole
) {
}