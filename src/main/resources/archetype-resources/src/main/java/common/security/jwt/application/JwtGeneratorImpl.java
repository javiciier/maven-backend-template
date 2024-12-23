package ${package}.common.security.jwt.application;

import ${package}.common.security.keys.RsaKeyManager;
import ${package}.common.security.jwt.domain.JwtData;
import ${package}.common.security.jwt.domain.JwtDataVisitor;
import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Log4j2
@NoArgsConstructor
@Component
@Primary
public class JwtGeneratorImpl implements JwtGenerator {
    @Value("${project.security.jwt.expirationInMilliseconds}")
    private long expirationInMilliseconds;
    @Value("${spring.application.name}")
    private String applicationName;


    public static Map<String, Object> toClaims(JwtData data) {
        return JwtDataVisitor.toMap(data);
    }

    @Override
    public String generateJWT(JwtData data) {
        Date currentDate = new Date();
        Date tokenExpirationDate = new Date(System.currentTimeMillis() + expirationInMilliseconds);
        PrivateKey privateKey = RsaKeyManager.loadKeyPair().getPrivate();

        Map<String, Object> customClaims = toClaims(data);
        log.info("Generating JWT for user {}", data.getUserID());

        return Jwts.builder()
                .signWith(privateKey)
                .issuer(applicationName)
                .subject(applicationName)
                .expiration(tokenExpirationDate)
                .issuedAt(currentDate)
                .header()
                .empty()
                .and()
                .claim("claims", customClaims)
                .compact();
    }


    @Override
    @SuppressWarnings("unchecked")
    public JwtData extractData(String token) {
        PublicKey publicKey = RsaKeyManager.loadKeyPair().getPublic();

        JwtParser jwtParser = Jwts.parser()
                .requireIssuer(applicationName)
                .requireSubject(applicationName)
                .verifyWith(publicKey)
                .build();

        Claims claims = jwtParser
                .parseSignedClaims(token)
                .getPayload();
        Map<String, Object> customClaims = claims.get("claims", Map.class);

        return JwtDataVisitor.fromMap(customClaims);
    }
}
