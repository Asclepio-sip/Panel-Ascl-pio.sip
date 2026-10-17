package Asclepio.MovimentacaoEstoque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * O Hibernate cria no Postgres um CHECK com os valores do enum MOV_TIPO quando a tabela nasce,
 * e o ddl-auto=update não atualiza esse CHECK depois. Sem isto, tipo novo (ex.: DEVOLUCAO_PEDIDO)
 * dá erro de constraint ao salvar. O enum continua validado pelo Java.
 */
@Component
@Order(0)
public class MovimentacaoTipoConstraintFix implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MovimentacaoTipoConstraintFix.class);

    private final JdbcTemplate jdbcTemplate;

    public MovimentacaoTipoConstraintFix(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            jdbcTemplate.execute(
                    "ALTER TABLE tb_movimentacao_estoque DROP CONSTRAINT IF EXISTS tb_movimentacao_estoque_mov_tipo_check");
        } catch (Exception e) {
            log.warn("Não foi possível remover o CHECK de MOV_TIPO: {}", e.getMessage());
        }
    }
}
