package Asclepio.config.security;

import Asclepio.UserLoja.UserLoja;
import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.User.User;
import Asclepio.exception.BusinessException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(
            User user,
            Long empresaId,
            Long lojaId,
            List<String> permissions,
            boolean gerente
    ) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withClaim("tipo", "FULL")
                    .withClaim("empresaId", empresaId)
                    .withClaim("lojaId", lojaId)
                    .withClaim("permissions", permissions)
                    .withClaim("gerente", gerente)
                    .withClaim("nome", user.getNome())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateTempToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withClaim("tipo", "TEMP")
                    .withExpiresAt(genExpirationDateTemp())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token temporário", e);
        }
    }

    private Instant genExpirationDateTemp() {
        return LocalDateTime.now()
                .plusMinutes(5) // token curto, só pra escolher a loja
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public DecodedJWT decodeToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token);
    }


}
