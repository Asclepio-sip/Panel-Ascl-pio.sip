package Asclepio.Produto;

import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import Asclepio.exception.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProdutoService {

    private final ProdutoStorageClient produtoStorageClient;


    public ProdutoService(
            ProdutoStorageClient produtoStorageClient
    ) {
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



        return produtoStorageClient.criarProduto(
                nome,
                descricao,
                marca,
                categoriaId,
                imagem
        );
    }

    public ProdutoStorageResponse atualizarImagemComStorage(Long id, MultipartFile imagem) {

        if (id == null) {
            throw new BusinessException("ID do produto é obrigatório");
        }

        if (imagem == null || imagem.isEmpty()) {
            throw new BusinessException("Imagem é obrigatória");
        }

        return produtoStorageClient.atualizarImagem(id, imagem);
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