package Asclepio.exception;

/** O usuário foi desativado pelo dono/gerente da conta (PUT /user/{id}/ativo). Vira 403. */
public class UsuarioDesativadoException extends RuntimeException {

    public static final String MENSAGEM =
            "Seu usuário está desativado. Fale com o responsável pela sua loja.";

    public UsuarioDesativadoException() {
        super(MENSAGEM);
    }
}
