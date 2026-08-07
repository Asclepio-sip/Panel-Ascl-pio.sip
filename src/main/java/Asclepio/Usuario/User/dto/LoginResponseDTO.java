package Asclepio.Usuario.User.dto;

import java.util.List;

public record LoginResponseDTO(

        String token,

        boolean escolherLoja,

        List<LojaLoginDTO> lojas

) {}