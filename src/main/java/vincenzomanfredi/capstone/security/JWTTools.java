package vincenzomanfredi.capstone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vincenzomanfredi.capstone.admin.entities.Admin;

import java.util.Date;

@Component
public class JWTTools {
    private final String secret;

    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(Admin admin) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))     //Data emissione
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))   //Data di scadenza
                .subject(String.valueOf(admin.getId()))      //id admin proprietario del token
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))     //Firma del token per garantire l'integrità
                .compact();
    }

    public void verifyToken() {
    }
}
