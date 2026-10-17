package Asclepio.Plano;

import java.text.Normalizer;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lê do plano o que o sistema controla. Os planos são cadastrados no painel SaaS como uma tabela de
 * comparação (nome do recurso / incluído / detalhe), então as regras saem dos recursos com estes nomes
 * (comparação sem acento e sem diferenciar maiúsculas):
 * <ul>
 *   <li>"Número de Lojas" e "Funcionários": o primeiro número do detalhe é o limite ("1", "Até 3");
 *       sem número ("Ilimitado") = sem limite; recurso marcado como não incluído = 0.</li>
 *   <li>"Cadastro de Clientes": liga/desliga o cadastro de clientes.</li>
 * </ul>
 * Empresa sem plano, ou plano sem o recurso, não tem limite (contas antigas continuam funcionando).
 */
public final class RegrasPlano {

    public static final String LOJAS = "Número de Lojas";
    public static final String FUNCIONARIOS = "Funcionários";
    public static final String CADASTRO_CLIENTES = "Cadastro de Clientes";

    private static final Pattern NUMERO = Pattern.compile("\\d+");

    private RegrasPlano() {
    }

    /** Limite do recurso ou null quando não há limite. */
    public static Integer limite(Plano plano, String recurso) {
        return buscar(plano, recurso)
                .map(r -> {
                    if (!r.isIncluido()) {
                        return 0;
                    }
                    Matcher numero = NUMERO.matcher(r.getDetalhe());
                    return numero.find() ? Integer.valueOf(numero.group()) : null;
                })
                .orElse(null);
    }

    public static boolean inclui(Plano plano, String recurso) {
        return buscar(plano, recurso).map(RecursoPlano::isIncluido).orElse(true);
    }

    private static Optional<RecursoPlano> buscar(Plano plano, String recurso) {
        if (plano == null) {
            return Optional.empty();
        }
        String procurado = normalizar(recurso);
        return plano.getRecursos().stream()
                .filter(r -> normalizar(r.getNome()).equals(procurado))
                .findFirst();
    }

    private static String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase();
    }
}
