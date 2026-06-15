package Asclepio.Loja.Bairro;

import Asclepio.Loja.Bairro.Bairro;
import Asclepio.Loja.Bairro.dto.BairroFiltroDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BairroSpecification {

    private BairroSpecification() {
    }

    public static Specification<Bairro> filtrar(BairroFiltroDTO filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.id() != null) {
                predicates.add(
                        cb.equal(root.get("id"), filtro.id())
                );
            }

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nome")),
                                "%" + filtro.nome().toLowerCase(Locale.ROOT).trim() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}