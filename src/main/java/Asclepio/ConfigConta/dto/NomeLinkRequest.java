package Asclepio.ConfigConta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NomeLinkRequest(
        @NotBlank(message = "Informe o nome do link")
        @Size(min = 3, max = 60, message = "O nome do link deve ter entre 3 e 60 caracteres")
        @Pattern(
                regexp = "^[a-z0-9]+(-[a-z0-9]+)*$",
                message = "Use apenas letras minúsculas, números e hífen (ex.: farmacia-central)")
        String nomeLink
) {
}
