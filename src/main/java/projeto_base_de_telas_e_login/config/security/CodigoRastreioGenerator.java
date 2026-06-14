package projeto_base_de_telas_e_login.config.security;

import java.security.SecureRandom;

public class CodigoRastreioGenerator {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String gerar() {
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < 20; i++) {
            int index = RANDOM.nextInt(CHARS.length());
            codigo.append(CHARS.charAt(index));
        }

        return codigo.toString();
    }
}