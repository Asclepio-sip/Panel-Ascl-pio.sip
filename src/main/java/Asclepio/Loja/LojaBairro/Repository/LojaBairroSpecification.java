package Asclepio.Loja.LojaBairro.Repository;

import Asclepio.Loja.LojaBairro.LojaBairro;
import Asclepio.Loja.LojaBairro.dto.LojaBairroFiltroDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class LojaBairroSpecification {

    private LojaBairroSpecification() {
    }

    public static Specification<LojaBairro> filtrar(
            LojaBairroFiltroDTO filtro,
            Long empresaId
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(root.get("loja").get("empresa").get("id"), empresaId)
            );

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.id() != null) {
                predicates.add(cb.equal(root.get("id"), filtro.id()));
            }

            if (filtro.lojaId() != null) {
                predicates.add(cb.equal(root.get("loja").get("id"), filtro.lojaId()));
            }

            if (filtro.bairroId() != null) {
                predicates.add(cb.equal(root.get("bairro").get("id"), filtro.bairroId()));
            }

            if (filtro.nomeLoja() != null && !filtro.nomeLoja().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("loja").get("nomeLoja")),
                                "%" + filtro.nomeLoja().trim().toLowerCase() + "%"
                        )
                );
            }

            if (filtro.nomeBairro() != null && !filtro.nomeBairro().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("bairro").get("nome")),
                                "%" + filtro.nomeBairro().trim().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}