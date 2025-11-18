package com.gymnasio.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  // Cambia este secreto por uno más largo/seguro (>=32 bytes para HS256)
  private static final String SECRET = "5aZq9vJrT4m8W2x7P0bC6nY1uE3kD8hQ";
  private static final int EXP_MINUTES = 120;

  private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

  public String generar(String username, String rolUnico) {
    // Si luego manejas múltiples roles, pásalos como lista
    List<String> roles = List.of(rolUnico);

    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(username)
        .claim("roles", roles) // ["ADMIN"] / ["CLIENTE"] / ...
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(EXP_MINUTES, ChronoUnit.MINUTES)))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean esValido(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String getUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  @SuppressWarnings("unchecked")
  public List<String> getRoles(String token) {
    Object claim = Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().get("roles");
    if (claim instanceof List<?> list) {
      return list.stream().map(Object::toString).toList();
    }
    return Collections.emptyList();
  }
}
