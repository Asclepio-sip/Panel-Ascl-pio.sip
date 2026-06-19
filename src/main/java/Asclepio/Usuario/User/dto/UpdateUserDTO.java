package Asclepio.Usuario.User.dto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record UpdateUserDTO(

        @NotBlank
        String login,

        String password,

        UUID roleId,

        List<UUID> permissionIds
) {
}