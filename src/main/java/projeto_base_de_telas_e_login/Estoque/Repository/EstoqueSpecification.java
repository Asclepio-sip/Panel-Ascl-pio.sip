package projeto_base_de_telas_e_login.Estoque.Repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import projeto_base_de_telas_e_login.Estoque.Estoque;
import projeto_base_de_telas_e_login.Estoque.dto.EstoqueFiltro;

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

                predicates.add(cb.equal(root.get("loja").get("id"), filtro.lojaId()));
            }

            if (filtro.nomeLoja() != null && !filtro.nomeLoja().isBlank()) {

                predicates.add(cb.like(cb.lower(root.get("loja").get("nome")), "%" + filtro.nomeLoja().trim().toLowerCase(Locale.ROOT) + "%"));
            }

            if (filtro.nomeProduto() != null && !filtro.nomeProduto().isBlank()) {

                predicates.add(cb.like(cb.lower(root.get("produto").get("name")), "%" + filtro.nomeProduto().trim().toLowerCase(Locale.ROOT) + "%"));
            }

            if (Boolean.TRUE.equals(filtro.semEstoque())) {

                predicates.add(cb.equal(root.get("quantidade"), 0));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}