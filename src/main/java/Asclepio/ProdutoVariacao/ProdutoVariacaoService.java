package Asclepio.ProdutoVariacao;

import Asclepio.ProdutoVariacao.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoVariacaoService {

    private final ProdutoVariacaoStorageClient storageClient;

    public ProdutoVariacaoService(ProdutoVariacaoStorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public ProdutoVariacaoResponseDTO criar(Long produtoId, ProdutoVariacaoAddDTO dto) {
        return storageClient.criar(produtoId, dto);
    }

    public ProdutoVariacaoPageResponse listar(ProdutoVariacaoFiltro filtro, Pageable pageable) {
        return storageClient.listar(filtro, pageable);
    }

    public ProdutoVariacaoResponseDTO atualizar(Long id, ProdutoVariacaoUpdateDTO dto) {
        return storageClient.atualizar(id, dto);
    }

    public void deletar(Long id) {
        storageClient.deletar(id);
    }
}