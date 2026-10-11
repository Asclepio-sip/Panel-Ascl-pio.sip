package Asclepio.exception;

/** Empresa bloqueada pelo painel SaaS (mensalidade não paga): o catálogo público sai do ar. Vira 403. */
public class CatalogoDesativadoException extends RuntimeException {

    public CatalogoDesativadoException() {
        super("Catálogo desativado temporariamente.");
    }
}
