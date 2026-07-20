package Asclepio.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
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


    public String generateToken(UsuarioAutenticado usuario) {

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);


            List<String> permissions = usuario.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();


            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuario.getUsername())

                    .withClaim(
                            "permissions",
                            permissions
                    )

                    .withClaim(
                            "empresaId",
                            usuario.getEmpresaId()
                    )

                    .withExpiresAt(genExpirationDate())

                    .sign(algorithm);


        } catch (JWTCreationException e) {

            throw new RuntimeException(
                    "Erro ao gerar token",
                    e
            );
        }
    }


    public String validateToken(String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();


        } catch (Exception e) {

            return null;
        }
    }


    private Instant genExpirationDate() {

        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(
                        ZoneOffset.of("-03:00")
                );
    }
}