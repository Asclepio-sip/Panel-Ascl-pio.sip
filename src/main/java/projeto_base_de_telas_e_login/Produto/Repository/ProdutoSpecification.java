package projeto_base_de_telas_e_login.Produto.Repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import projeto_base_de_telas_e_login.Produto.Product;
import projeto_base_de_telas_e_login.Produto.dto.ProdutoFiltro;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdutoSpecification {

    private ProdutoSpecification() {
    }

    public static Specification<Product> filtrar(ProdutoFiltro filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filtro.nome().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.variacao() != null && !filtro.variacao().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("variacao")),
                                "%" + filtro.variacao().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.categoriaId() != null) {
                predicates.add(
                        cb.equal(root.get("categoria").get("id"), filtro.categoriaId())
                );
            }

            if (filtro.nomeCategoria() != null && !filtro.nomeCategoria().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("categoria").get("nomeCategoria")),
                                "%" + filtro.nomeCategoria().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}