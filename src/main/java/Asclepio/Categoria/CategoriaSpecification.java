package Asclepio.Categoria;
import Asclepio.Categoria.dto.CategoriaFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;


public class CategoriaSpecification {

    public static Specification<Categoria> filtrar(
            CategoriaFiltro filtro
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.nome() != null &&
                    !filtro.nome().isBlank()) {

                predicates.add(

                        cb.like(

                                cb.lower(
                                        root.get("nomeCategoria")
                                ),

                                "%" +
                                        filtro.nome().toLowerCase()
                                        + "%"
                        )
                );
            }

            if (filtro.categoriaPaiId() != null) {

                predicates.add(

                        cb.equal(

                                root.get("categoriaPai")
                                        .get("id"),

                                filtro.categoriaPaiId()
                        )
                );
            }

            if (Boolean.TRUE.equals(
                    filtro.somentePrincipais()
            )) {

                predicates.add(

                        cb.isNull(
                                root.get("categoriaPai")
                        )
                );
            }

            return cb.and(
                    predicates.toArray(
                            Predicate[]::new
                    )
            );
        };
    }
}