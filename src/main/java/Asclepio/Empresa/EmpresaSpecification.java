package Asclepio.Empresa;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.dto.EmpresaFiltroDTO;
import org.springframework.data.jpa.domain.Specification;

public class EmpresaSpecification {

    public static Specification<Empresa> filtrar(EmpresaFiltroDTO filtro) {
        return (root, query, builder) -> {
            var predicates = builder.conjunction();

            if (filtro == null) {
                return predicates;
            }

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates = builder.and(
                        predicates,
                        builder.like(
                                builder.lower(root.get("nome")),
                                "%" + filtro.nome().toLowerCase() + "%"
                        )
                );
            }

            if (filtro.cnpj() != null && !filtro.cnpj().isBlank()) {
                predicates = builder.and(
                        predicates,
                        builder.equal(root.get("cnpj"), filtro.cnpj().trim())
                );
            }

            if (filtro.ativa() != null) {
                predicates = builder.and(
                        predicates,
                        builder.equal(root.get("ativa"), filtro.ativa())
                );
            }

            return predicates;
        };
    }
}