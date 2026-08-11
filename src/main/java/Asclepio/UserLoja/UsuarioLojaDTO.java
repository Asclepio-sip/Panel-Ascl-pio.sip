package Asclepio.UserLoja;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UsuarioLojaDTO(
        @NotNull
        Long lojaId,
        @NotNull
        UUID roleId
) {}