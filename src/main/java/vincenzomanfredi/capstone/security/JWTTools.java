package vincenzomanfredi.capstone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vincenzomanfredi.capstone.exceptions.Unauthorized;
import vincenzomanfredi.capstone.utente.entities.Utente;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {
    private final String secret;

    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(Utente utente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))     //Data emissione
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))   //Data di scadenza
                .subject(String.valueOf(utente.getId()))      //id admin proprietario del token
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))     //Firma del token per garantire l'integrità
                .compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            throw new Unauthorized("Ci sono stati problemi con il token! Rieffettuare login!");
        }
    }

    public UUID extractIdFromToken(String token) {
        return UUID.fromString(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build()
                .parseSignedClaims(token).getPayload().getSubject());
    }
}




