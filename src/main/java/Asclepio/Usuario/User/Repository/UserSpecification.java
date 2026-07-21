package Asclepio.Usuario.User.Repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.User.User;
import Asclepio.Usuario.User.dto.UserFiltroDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> filtrar(
            UserFiltroDTO filtro,
            Long empresaId
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("empresa").get("id"),
                            empresaId
                    )
            );


            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.login() != null && !filtro.login().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("username")),
                                "%" + filtro.login().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.ativo() != null) {
                predicates.add(
                        cb.equal(root.get("ativo"), filtro.ativo())
                );
            }

            Join<User, Role> roleJoin = root.join("role", JoinType.LEFT);

            if (filtro.roleId() != null) {
                predicates.add(
                        cb.equal(roleJoin.get("id"), filtro.roleId())
                );
            }

            if (filtro.nomeRole() != null && !filtro.nomeRole().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(roleJoin.get("nome")),
                                "%" + filtro.nomeRole().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}