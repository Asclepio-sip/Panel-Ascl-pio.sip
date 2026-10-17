package Asclepio.Plano;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegrasPlanoTest {

    private static Plano plano(RecursoPlano... recursos) {
        Plano plano = new Plano(1L);
        plano.atualizar("Teste", "", BigDecimal.ZERO, false, List.of(recursos));
        return plano;
    }

    @Test
    void limiteEhOPrimeiroNumeroDoDetalhe() {
        Plano plano = plano(
                new RecursoPlano("Número de Lojas", true, "1"),
                new RecursoPlano("Funcionários", true, "Até 5"));

        assertEquals(1, RegrasPlano.limite(plano, RegrasPlano.LOJAS));
        assertEquals(5, RegrasPlano.limite(plano, RegrasPlano.FUNCIONARIOS));
    }

    @Test
    void semNumeroEhIlimitado() {
        Plano plano = plano(new RecursoPlano("Número de Lojas", true, "Ilimitado"));

        assertNull(RegrasPlano.limite(plano, RegrasPlano.LOJAS));
    }

    @Test
    void recursoNaoIncluidoTemLimiteZero() {
        Plano plano = plano(new RecursoPlano("Funcionários", false, ""));

        assertEquals(0, RegrasPlano.limite(plano, RegrasPlano.FUNCIONARIOS));
    }

    @Test
    void nomeIgnoraAcentoEMaiusculas() {
        Plano plano = plano(
                new RecursoPlano("numero de lojas", true, "Até 3"),
                new RecursoPlano("CADASTRO DE CLIENTES", false, ""));

        assertEquals(3, RegrasPlano.limite(plano, RegrasPlano.LOJAS));
        assertFalse(RegrasPlano.inclui(plano, RegrasPlano.CADASTRO_CLIENTES));
    }

    @Test
    void semPlanoOuSemRecursoNaoTemLimite() {
        assertNull(RegrasPlano.limite(null, RegrasPlano.LOJAS));
        assertTrue(RegrasPlano.inclui(null, RegrasPlano.CADASTRO_CLIENTES));
        assertNull(RegrasPlano.limite(plano(), RegrasPlano.FUNCIONARIOS));
        assertTrue(RegrasPlano.inclui(plano(), RegrasPlano.CADASTRO_CLIENTES));
    }
}
