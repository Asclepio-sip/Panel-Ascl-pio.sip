package Asclepio.exception;

/** A empresa foi bloqueada pelo painel SaaS (ex.: mensalidade não paga). Vira 403. */
public class ContaBloqueadaException extends RuntimeException {

    public static final String MENSAGEM =
            "Acesso bloqueado por falta de pagamento da mensalidade. Entre em contato com o suporte.";

    public ContaBloqueadaException() {
        super(MENSAGEM);
    }
}
