package Asclepio.Usuario.User.Repository;

import Asclepio.UserLoja.UserLoja;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.User.User;
import Asclepio.Usuario.User.dto.UserFiltroDTO;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

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


            // ==================================================
            // USER -> USER_LOJA -> LOJA -> EMPRESA
            // ==================================================

            Join<User, UserLoja> userLojaJoin =
                    root.join("userLojas", JoinType.INNER);

            Join<UserLoja, ?> lojaJoin =
                    userLojaJoin.join("loja", JoinType.INNER);

            Join<?, ?> empresaJoin =
                    lojaJoin.join("empresa", JoinType.INNER);


            // ==================================================
            // FILTRAR PELA EMPRESA DO USUÁRIO LOGADO
            // ==================================================

            predicates.add(
                    cb.equal(
                            empresaJoin.get("id"),
                            empresaId
                    )
            );


            // ==================================================
            // SE NÃO TIVER FILTRO
            // ==================================================

            if (filtro == null) {

                query.distinct(true);

                return cb.and(
                        predicates.toArray(Predicate[]::new)
                );
            }


            // ==================================================
            // FILTRO POR LOGIN
            // ==================================================

            if (filtro.login() != null &&
                    !filtro.login().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(
                                        root.get("username")
                                ),
                                "%" +
                                        filtro.login()
                                                .trim()
                                                .toLowerCase(Locale.ROOT) +
                                        "%"
                        )
                );
            }


            // ==================================================
            // FILTRO POR ATIVO
            // ==================================================

            if (filtro.ativo() != null) {

                predicates.add(
                        cb.equal(
                                root.get("ativo"),
                                filtro.ativo()
                        )
                );
            }


            // ==================================================
            // USER_LOJA -> ROLE
            // ==================================================

            Join<UserLoja, Role> roleJoin =
                    userLojaJoin.join(
                            "role",
                            JoinType.LEFT
                    );


            // ==================================================
            // FILTRO POR ROLE ID
            // ==================================================

            if (filtro.roleId() != null) {

                predicates.add(
                        cb.equal(
                                roleJoin.get("id"),
                                filtro.roleId()
                        )
                );
            }


            // ==================================================
            // FILTRO POR NOME DA ROLE
            // ==================================================

            if (filtro.nomeRole() != null &&
                    !filtro.nomeRole().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(
                                        roleJoin.get("nome")
                                ),
                                "%" +
                                        filtro.nomeRole()
                                                .trim()
                                                .toLowerCase(Locale.ROOT) +
                                        "%"
                        )
                );
            }


            // ==================================================
            // EVITAR USUÁRIOS DUPLICADOS
            // ==================================================

            query.distinct(true);


            return cb.and(
                    predicates.toArray(Predicate[]::new)
            );
        };
    }
}

