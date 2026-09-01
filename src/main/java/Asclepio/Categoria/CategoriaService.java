package Asclepio.Categoria;

import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.exception.BusinessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final EmpresaContext empresaContext;

    public CategoriaService(
            CategoriaRepository repository,
            EmpresaContext empresaContext
    ) {
        this.repository = repository;
        this.empresaContext = empresaContext;
    }

    public CategoriaPageResponse listar(
            CategoriaFiltro filtro,
            Pageable pageable
    ) {

        Long empresaId = empresaContext.getEmpresaId();

        Page<Categoria> page =
                repository.findAll(
                        CategoriaSpecification.filtrar(
                                filtro,
                                empresaId
                        ),
                        pageable
                );

        List<CategoriaResponse> content =
                page.getContent()
                        .stream()
                        .map(CategoriaResponse::fromEntity)
                        .toList();

        return new CategoriaPageResponse(
                content,
                new CategoriaPageResponse.PageInfo(
                        page.getSize(),
                        page.getNumber(),
                        page.getTotalElements(),
                        page.getTotalPages()
                )
        );
    }

    public CategoriaResponse criar(CriarCategoria dto) {

        validarDto(dto);

        Long empresaId = empresaContext.getEmpresaId();

        String nome = dto.nomeCategoria().trim();

        if (repository.existsByNomeCategoriaAndEmpresaId(
                nome,
                empresaId
        )) {
            throw new BusinessException(
                    "Categoria já existe nessa empresa"
            );
        }

        Categoria categoriaPai = null;

        if (dto.categoriaPaiId() != null) {

            categoriaPai =
                    buscarPorId(dto.categoriaPaiId());

            if (!categoriaPai.getEmpresaId().equals(empresaId)) {
                throw new BusinessException(
                        "Categoria pai não pertence à empresa logada"
                );
            }
        }

        Categoria categoria = new Categoria();

        categoria.setNomeCategoria(nome);
        categoria.setDescricao(dto.descricao());
        categoria.setIcone(dto.icone());
        categoria.setCategoriaPai(categoriaPai);
        categoria.setAtiva(true);
        categoria.setEmpresaId(empresaId);

        Categoria salva = repository.save(categoria);

        return CategoriaResponse.fromEntity(salva);
    }

    public CategoriaResponse editar(Long id, CriarCategoria dto) {

        if (id == null) {
            throw new BusinessException("ID da categoria é obrigatório");
        }

        validarDto(dto);

        Long empresaId = empresaContext.getEmpresaId();

        Categoria categoria = buscarPorId(id);

        if (dto.nomeCategoria() != null && !dto.nomeCategoria().isBlank()) {

            String novoNome = dto.nomeCategoria().trim();

            repository
                    .findByNomeCategoriaAndEmpresaId(novoNome, empresaId)
                    .ifPresent(existente -> {

                        if (!existente.getId().equals(categoria.getId())) {
                            throw new BusinessException(
                                    "Categoria já existe nessa empresa"
                            );
                        }
                    });

            categoria.setNomeCategoria(novoNome);
        }

        if (dto.descricao() != null) {

            String descricao = dto.descricao().trim();

            if (!descricao.isBlank()
                    && !descricao.equalsIgnoreCase("string")) {

                categoria.setDescricao(descricao);
            }
        }

        if (dto.icone() != null) {

            String icone = dto.icone().trim();

            if (!icone.isBlank()
                    && !icone.equalsIgnoreCase("string")) {

                categoria.setIcone(icone);
            }
        }

        if (dto.categoriaPaiId() != null) {

            Categoria categoriaPai =
                    buscarPorId(dto.categoriaPaiId());

            if (categoriaPai.getId().equals(categoria.getId())) {
                throw new BusinessException(
                        "A categoria não pode ser pai dela mesma"
                );
            }

            if (!categoriaPai.getEmpresaId().equals(empresaId)) {
                throw new BusinessException(
                        "Categoria pai não pertence à empresa logada"
                );
            }

            categoria.setCategoriaPai(categoriaPai);
        }

        Categoria atualizada = repository.save(categoria);

        return CategoriaResponse.fromEntity(atualizada);
    }

    public void deletar(Long id) {

        if (id == null) {
            throw new BusinessException(
                    "ID da categoria é obrigatório"
            );
        }

        Categoria categoria = buscarPorId(id);

        if (!categoria.getSubcategorias().isEmpty()) {

            throw new BusinessException(
                    "Categoria possui subcategorias vinculadas"
            );
        }

        try {

            repository.delete(categoria);

        } catch (DataIntegrityViolationException e) {

            throw new BusinessException(
                    "Categoria possui produtos vinculados"
            );
        }
    }

    private Categoria buscarPorId(Long id) {

        if (id == null) {
            throw new BusinessException(
                    "ID da categoria é obrigatório"
            );
        }

        Long empresaId = empresaContext.getEmpresaId();

        return repository
                .findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Categoria não encontrada com id: " + id
                        )
                );
    }
    private void validarDto(CriarCategoria dto) {

        if (dto == null) {
            throw new BusinessException(
                    "Dados da categoria são obrigatórios"
            );
        }

        if (dto.nomeCategoria() == null
                || dto.nomeCategoria().isBlank()
                || dto.nomeCategoria().trim().equalsIgnoreCase("string")) {

            throw new BusinessException(
                    "Nome da categoria é obrigatório"
            );
        }

        if (dto.nomeCategoria().trim().length() > 100) {
            throw new BusinessException(
                    "Nome da categoria deve ter no máximo 100 caracteres"
            );
        }

        if (dto.descricao() != null
                && dto.descricao().trim().length() > 500) {

            throw new BusinessException(
                    "Descrição deve ter no máximo 500 caracteres"
            );
        }

        if (dto.icone() != null
                && dto.icone().trim().length() > 100) {

            throw new BusinessException(
                    "Ícone deve ter no máximo 100 caracteres"
            );
        }
    }

}