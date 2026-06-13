package projeto_base_de_telas_e_login.Pedido.Repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import projeto_base_de_telas_e_login.Pedido.Pedido;
import projeto_base_de_telas_e_login.Pedido.dto.PedidoFiltro;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PedidoSpecification {

    private PedidoSpecification() {
    }

    public static Specification<Pedido> filtrar(PedidoFiltro filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.lojaId() != null) {
                predicates.add(
                        cb.equal(root.get("loja").get("id"), filtro.lojaId())
                );
            }

            if (filtro.nomeLoja() != null && !filtro.nomeLoja().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("loja").get("nomeLoja")),
                                "%" + filtro.nomeLoja().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.nomeCliente() != null && !filtro.nomeCliente().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nomeCliente")),
                                "%" + filtro.nomeCliente().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.telefone() != null && !filtro.telefone().isBlank()) {
                predicates.add(
                        cb.like(
                                root.get("telefone"),
                                "%" + filtro.telefone().trim() + "%"
                        )
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

            if (filtro.bairro() != null && !filtro.bairro().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("bairro")),
                                "%" + filtro.bairro().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.status() != null) {
                predicates.add(cb.equal(root.get("status"), filtro.status()));
            }

            if (filtro.tipoEntrega() != null) {
                predicates.add(cb.equal(root.get("tipoEntrega"), filtro.tipoEntrega()));
            }

            if (filtro.formaDePagamento() != null) {
                predicates.add(cb.equal(root.get("formaDePagamento"), filtro.formaDePagamento()));
            }

            if (filtro.freteGratis() != null) {
                predicates.add(cb.equal(root.get("freteGratis"), filtro.freteGratis()));
            }

            if (filtro.dataInicio() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("criadoEm"),
                                filtro.dataInicio().atStartOfDay()
                        )
                );
            }

            if (filtro.dataFim() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("criadoEm"),
                                filtro.dataFim().atTime(LocalTime.MAX)
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}