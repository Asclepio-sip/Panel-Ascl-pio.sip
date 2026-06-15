package Asclepio.Usuario.User.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String login,
        String role,
        String password
) {
}
