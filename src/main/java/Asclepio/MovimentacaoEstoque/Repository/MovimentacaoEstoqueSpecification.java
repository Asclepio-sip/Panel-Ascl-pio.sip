package Asclepio.MovimentacaoEstoque.Repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovimentacaoEstoqueSpecification {

    private MovimentacaoEstoqueSpecification() {
    }

    public static Specification<MovimentacaoEstoque> filtrar(
            MovimentacaoEstoqueFiltro filtro,
            Long empresaId
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("estoque")
                                    .get("loja")
                                    .get("empresa")
                                    .get("id"),
                            empresaId
                    )
            );


            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.lojaId() != null) {
                predicates.add(
                        cb.equal(root.get("loja").get("id"), filtro.lojaId())
                );
            }

            if (filtro.produtoId() != null) {
                predicates.add(
                        cb.equal(root.get("produto").get("id"), filtro.produtoId())
                );
            }

            if (filtro.estoqueId() != null) {
                predicates.add(
                        cb.equal(root.get("estoque").get("id"), filtro.estoqueId())
                );
            }

            if (filtro.nomeLoja() != null && !filtro.nomeLoja().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("loja").get("nomeLoja")),
                                "%" + filtro.nomeLoja().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.nomeProduto() != null && !filtro.nomeProduto().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("produto").get("name")),
                                "%" + filtro.nomeProduto().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.usuario() != null && !filtro.usuario().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("usuario").get("username")),
                                "%" + filtro.usuario().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.tipo() != null) {
                predicates.add(
                        cb.equal(root.get("tipo"), filtro.tipo())
                );
            }

            if (filtro.dataInicio() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("criadoEm"),
                                filtro.dataInicio().atStartOfDay()
                        )
                );
            }

            if (filtro.dataFim() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("criadoEm"),
                                filtro.dataFim().atTime(LocalTime.MAX)
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}