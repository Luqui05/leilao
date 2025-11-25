package br.com.lucas.leilao.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

  public String gerarToken(Authentication authentication) {
    String email = authentication.getName();
    Date agora = new Date();
    Date expiracao = new Date(agora.getTime() + exp);

    List<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return Jwts.builder()
        .setSubject(email)
        .claim("roles", roles)
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
