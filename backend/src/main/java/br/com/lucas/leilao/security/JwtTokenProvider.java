package br.com.lucas.leilao.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String segredo;

  @Value("${jwt.expiration}")
  private long exp;

  private SecretKey getChaveAssinatura() {
    return Keys.hmacShaKeyFor(segredo.getBytes());
  }

  public String gerarToken(String email) {
    Date agora = new Date();
    Date expiracao = new Date(agora.getTime() + exp);

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(agora)
        .setExpiration(expiracao)
        .signWith(getChaveAssinatura())
        .compact();
  }

  public String extrairEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getChaveAssinatura())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean tokenValido(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getChaveAssinatura())
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
