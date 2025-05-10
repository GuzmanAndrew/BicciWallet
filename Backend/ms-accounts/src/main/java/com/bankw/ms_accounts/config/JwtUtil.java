package com.bankw.ms_accounts.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.function.Function;

@Component
public class JwtUtil {

  private static final String SECRET_KEY =
      "CWCG2FIY8pymB+mO51FG2H1wSKv8cSHfucVoZXTT/wg=";

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims =
        Jwts.parser()
            .verifyWith(getSigningKey())
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
