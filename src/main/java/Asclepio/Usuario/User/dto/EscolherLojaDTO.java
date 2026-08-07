package Asclepio.Usuario.User.dto;

import jakarta.validation.constraints.NotNull;

public record EscolherLojaDTO(

        @NotNull
        Long lojaId

) {
}