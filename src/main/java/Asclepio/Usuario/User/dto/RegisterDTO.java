package Asclepio.Usuario.User.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record RegisterDTO(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotNull
        UUID roleId,
        String Email,
        List<UUID> permissionIds
) {
}