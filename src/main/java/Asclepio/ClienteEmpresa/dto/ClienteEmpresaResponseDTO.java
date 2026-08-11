package Asclepio.ClienteEmpresa.dto;

import java.util.UUID;

public record ClienteEmpresaResponseDTO(

        UUID id,
        String nome,
        String numero,
        String email

) {}