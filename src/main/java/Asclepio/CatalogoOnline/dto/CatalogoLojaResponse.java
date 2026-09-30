package Asclepio.CatalogoOnline.dto;

import Asclepio.Loja.Loja.Loja;

// Resposta pública: só o necessário para o cliente escolher a loja.
public record CatalogoLojaResponse(
        Long id,
        String nomeLoja
) {

    public static CatalogoLojaResponse fromEntity(Loja loja) {
        return new CatalogoLojaResponse(
                loja.getId(),
                loja.getNomeLoja()
        );
    }
}
