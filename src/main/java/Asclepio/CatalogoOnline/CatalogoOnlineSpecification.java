package Asclepio.CatalogoOnline;

import Asclepio.CatalogoOnline.dto.CatalogoOnlineFiltro;
import Asclepio.Estoque.Estoque;
import Asclepio.ProdutoVariacao.ProdutoVariacao;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CatalogoOnlineSpecification {

    private CatalogoOnlineSpecification() {
    }

    // Só itens da loja, com quantidade > 0 e variação ativa.
    public static Specification<Estoque> filtrar(Long lojaId, CatalogoOnlineFiltro filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("loja").get("id"), lojaId));
            predicates.add(cb.greaterThan(root.get("quantidade"), 0));

            // Estoque guarda só o variacaoId (sem relacionamento), então filtra a variação via subquery
            Subquery<Long> variacoes = query.subquery(Long.class);
            Root<ProdutoVariacao> variacao = variacoes.from(ProdutoVariacao.class);

            List<Predicate> predicatesVariacao = new ArrayList<>();
            predicatesVariacao.add(cb.isTrue(variacao.get("ativo")));

            if (filtro != null && filtro.nomeProduto() != null && !filtro.nomeProduto().isBlank()) {
                predicatesVariacao.add(cb.like(cb.lower(variacao.get("produto").get("nome")), "%" + filtro.nomeProduto().trim().toLowerCase(Locale.ROOT) + "%"));
            }

            if (filtro != null && filtro.categoriaId() != null) {
                predicatesVariacao.add(cb.equal(variacao.get("produto").get("categoria").get("id"), filtro.categoriaId()));
            }

            variacoes.select(variacao.get("id")).where(predicatesVariacao.toArray(Predicate[]::new));

            predicates.add(root.get("variacaoId").in(variacoes));

            if (filtro != null && Boolean.TRUE.equals(filtro.somentePromocao())) {
                predicates.add(cb.greaterThan(root.get("percentualDesconto"), BigDecimal.ZERO));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
