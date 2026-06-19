package Asclepio.Categoria;

import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.exception.BusinessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaStorageClient categoriaStorageClient;

    public CategoriaService(CategoriaStorageClient categoriaStorageClient) {
        this.categoriaStorageClient = categoriaStorageClient;
    }

    public CategoriaPageResponse listar(CategoriaFiltro filtro, Pageable pageable) {
        return categoriaStorageClient.listar(filtro, pageable);
    }

    public CategoriaResponse criar(CriarCategoria dto) {
        validarDto(dto);

        return categoriaStorageClient.criar(dto);
    }

    public CategoriaResponse editar(Long id, CriarCategoria dto) {
        if (id == null) {
            throw new BusinessException("ID da categoria é obrigatório");
        }

        validarDto(dto);

        return categoriaStorageClient.editar(id, dto);
    }

    public void deletar(Long id) {
        if (id == null) {
            throw new BusinessException("ID da categoria é obrigatório");
        }

        categoriaStorageClient.deletar(id);
    }

    private void validarDto(CriarCategoria dto) {
        if (dto == null) {
            throw new BusinessException("Dados da categoria são obrigatórios");
        }

        if (dto.nomeCategoria() == null || dto.nomeCategoria().isBlank()) {
            throw new BusinessException("Nome da categoria é obrigatório");
        }
    }
}