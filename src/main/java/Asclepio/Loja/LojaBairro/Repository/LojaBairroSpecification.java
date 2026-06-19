package Asclepio.Loja.LojaBairro.Repository;

import Asclepio.Loja.Bairro.Bairro;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.LojaBairro.LojaBairro;
import Asclepio.Loja.LojaBairro.dto.LojaBairroFiltroDTO;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LojaBairroSpecification {

    private LojaBairroSpecification() {
    }

    public static Specification<LojaBairro> filtrar(LojaBairroFiltroDTO filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            Join<LojaBairro, Loja> lojaJoin = root.join("loja", JoinType.LEFT);
            Join<LojaBairro, Bairro> bairroJoin = root.join("bairro", JoinType.LEFT);

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.id() != null) {
                predicates.add(
                        cb.equal(root.get("id"), filtro.id())
                );
            }

            if (filtro.lojaId() != null) {
                predicates.add(
                        cb.equal(lojaJoin.get("id"), filtro.lojaId())
                );
            }

            if (filtro.nomeLoja() != null && !filtro.nomeLoja().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(lojaJoin.get("nomeLoja")),
                                "%" + filtro.nomeLoja().toLowerCase(Locale.ROOT).trim() + "%"
                        )
                );
            }

            if (filtro.bairroId() != null) {
                predicates.add(
                        cb.equal(bairroJoin.get("id"), filtro.bairroId())
                );
            }

            if (filtro.nomeBairro() != null && !filtro.nomeBairro().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(bairroJoin.get("nome")),
                                "%" + filtro.nomeBairro().toLowerCase(Locale.ROOT).trim() + "%"
                        )
                );
            }

            if (filtro.valorFreteMin() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("valorFrete"),
                                filtro.valorFreteMin()
                        )
                );
            }

            if (filtro.valorFreteMax() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("valorFrete"),
                                filtro.valorFreteMax()
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}