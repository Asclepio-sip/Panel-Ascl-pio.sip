package Asclepio.Estoque.Repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.dto.EstoqueFiltro;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EstoqueSpecification {

    private EstoqueSpecification() {
    }

    public static Specification<Estoque> filtrar(EstoqueFiltro filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.lojaId() != null) {
                predicates.add(
                        cb.equal(root.get("loja").get("id"), filtro.lojaId())
                );
            }

            if (filtro.nomeLoja() != null && !filtro.nomeLoja().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("loja").get("nome")),
                                "%" + filtro.nomeLoja().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.nomeProduto() != null && !filtro.nomeProduto().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("produtoVariacao").get("produto").get("name")),
                                "%" + filtro.nomeProduto().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.nomeVariacao() != null && !filtro.nomeVariacao().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("produtoVariacao").get("nomeVariacao")),
                                "%" + filtro.nomeVariacao().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.categoriaId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("produtoVariacao").get("produto").get("categoria").get("id"),
                                filtro.categoriaId()
                        )
                );
            }

            if (filtro.nomeCategoria() != null && !filtro.nomeCategoria().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("produtoVariacao").get("produto").get("categoria").get("nomeCategoria")),
                                "%" + filtro.nomeCategoria().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (Boolean.TRUE.equals(filtro.semEstoque())) {
                predicates.add(
                        cb.equal(root.get("quantidade"), 0)
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}