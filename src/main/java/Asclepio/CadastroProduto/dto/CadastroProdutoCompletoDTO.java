package Asclepio.CadastroProduto.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record CadastroProdutoCompletoDTO(

        // Produto
        String nome,
        String descricao,
        String marca,
        Long categoriaId,

        MultipartFile imagem,

        // Variação
        String nomeVariacao,
        String codigoBarras,

        // Estoque
        Long lojaId,
        Integer quantidade,
        BigDecimal precoVenda

) {}