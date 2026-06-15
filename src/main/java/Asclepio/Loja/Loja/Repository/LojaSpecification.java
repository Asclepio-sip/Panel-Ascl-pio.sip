package Asclepio.Loja.Loja.Repository;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LojaSpecification {

    private LojaSpecification() {
    }

    public static Specification<Loja> filtrar(LojaFiltroDTO filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.id() != null) {
                predicates.add(cb.equal(root.get("id"), filtro.id()));
            }

            if (filtro.nomeLoja() != null && !filtro.nomeLoja().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nomeLoja")),
                                "%" + filtro.nomeLoja().toLowerCase(Locale.ROOT).trim() + "%"
                        )
                );
            }

            if (filtro.cep() != null && !filtro.cep().isBlank()) {
                predicates.add(
                        cb.like(root.get("cep"), "%" + filtro.cep().trim() + "%")
                );
            }

            if (filtro.cnpj() != null && !filtro.cnpj().isBlank()) {
                predicates.add(
                        cb.like(root.get("cnpj"), "%" + filtro.cnpj().trim() + "%")
                );
            }

            if (filtro.telefone() != null && !filtro.telefone().isBlank()) {
                predicates.add(
                        cb.like(root.get("telefone"), "%" + filtro.telefone().trim() + "%")
                );
            }

            if (filtro.tipoAtendimento() != null) {
                predicates.add(
                        cb.equal(root.get("tipoAtendimento"), filtro.tipoAtendimento())
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}