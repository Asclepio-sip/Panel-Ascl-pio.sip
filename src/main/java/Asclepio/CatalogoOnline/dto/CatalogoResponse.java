package Asclepio.CatalogoOnline.dto;

import Asclepio.ConfigConta.dto.DesignCatalogoResponse;

import java.util.List;

// Tudo o que o site público precisa para abrir a página do catálogo em uma chamada só.
public record CatalogoResponse(
        String nomeLink,
        String nomeEmpresa,
        DesignCatalogoResponse design,
        List<CatalogoLojaResponse> lojas,
        List<CatalogoCategoriaResponse> categorias
) {
}
