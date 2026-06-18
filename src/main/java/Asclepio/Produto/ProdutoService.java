package Asclepio.Produto;

import Asclepio.Categoria.Categoria;
import Asclepio.Categoria.CategoriaRepository;
import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProductoAddDto;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProdutoService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoStorageClient produtoStorageClient;


    public ProdutoService(
            CategoriaRepository categoriaRepository,
            ProdutoStorageClient produtoStorageClient
    ) {
        this.categoriaRepository = categoriaRepository;
        this.produtoStorageClient = produtoStorageClient;
    }


    public ProdutoStorageResponse criarComStorage(
            String nome,
            String descricao,
            String marca,
            Long categoriaId,
            MultipartFile imagem
    ) {
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("Nome do produto é obrigatório");
        }

        if (categoriaId == null) {
            throw new BusinessException("Categoria é obrigatória");
        }

        return produtoStorageClient.criarProduto(
                nome,
                descricao,
                marca,
                categoriaId,
                imagem
        );
    }


    public PageResponse<ProdutoStorageResponse> listarTodosStorage(
            ProdutoFiltro filtro,
            Pageable pageable
    ) {
        return produtoStorageClient.listarProdutos(
                filtro != null ? filtro.nome() : null,
                filtro != null ? filtro.categoriaId() : null,
                filtro != null ? filtro.nomeCategoria() : null,
                pageable
        );
    }


    public void deletarComStorage(Long id) {
        if (id == null) {
            throw new BusinessException("ID do produto é obrigatório");
        }

        produtoStorageClient.deletarProduto(id);
    }



    public ProdutoStorageResponse atualizarComStorage(
            Long id,
            ProdutoUpdateDto dto
    ) {

        if (id == null) {
            throw new BusinessException("ID do produto é obrigatório");
        }

        if (dto == null) {
            throw new BusinessException("Dados para atualização são obrigatórios");
        }

        return produtoStorageClient.atualizarProduto(id, dto);
    }


}