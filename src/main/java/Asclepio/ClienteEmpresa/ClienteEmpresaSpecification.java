package Asclepio.ClienteEmpresa;

import Asclepio.ClienteEmpresa.dto.ClienteEmpresaFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClienteEmpresaSpecification {

    private ClienteEmpresaSpecification() {
    }

    public static Specification<ClienteEmpresa> filtrar(ClienteEmpresaFiltro filtro, Long empresaId) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("empresa").get("id"), empresaId));

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nome")),
                                "%" + filtro.nome().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.numero() != null && !filtro.numero().isBlank()) {
                predicates.add(
                        cb.like(root.get("numero"), "%" + filtro.numero().trim() + "%")
                );
            }

            if (filtro.email() != null && !filtro.email().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("email")),
                                "%" + filtro.email().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}