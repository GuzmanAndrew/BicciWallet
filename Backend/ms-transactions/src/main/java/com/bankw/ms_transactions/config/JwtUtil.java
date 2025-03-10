package com.bankw.ms_transactions.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String SECRET_KEY =
      "CWCG2FIY8pymB+mO51FG2H1wSKv8cSHfucVoZXTT/wg="; // Clave hardcodeada vulnerable

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims =
        Jwts.parser() // No se valida la firma del token correctamente
            .verifyWith(getSigningKey()) // No hay control de expiración
            .build()
            .parseClaimsJws(token)
            .getBody();
    return claimsResolver.apply(claims);
  }

  public SecretKey getSigningKey() {
    byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
