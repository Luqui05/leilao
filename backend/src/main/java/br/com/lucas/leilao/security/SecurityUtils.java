package br.com.lucas.leilao.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.lucas.leilao.model.Pessoa;

@Component
public class SecurityUtils {

  public static Pessoa getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof Pessoa) {
      return (Pessoa) auth.getPrincipal();
    }
    return null;
  }

  public static Long getAuthenticatedUserId() {
    Pessoa pessoa = getAuthenticatedUser();
    return pessoa != null ? pessoa.getId() : null;
  }

  public static String getAuthenticatedUserEmail() {
    Pessoa pessoa = getAuthenticatedUser();
    return pessoa != null ? pessoa.getEmail() : null;
  }

  public static boolean hasRole(String role) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return false;
    }

    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
    return authorities.stream()
        .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
  }

  public static boolean isAdmin() {
    return hasRole("ADMIN");
  }

  public static boolean isVendedor() {
    return hasRole("VENDEDOR");
  }

  public static boolean isComprador() {
    return hasRole("COMPRADOR");
  }

  public static boolean isOwnerOrAdmin(Long resourceOwnerId) {
    if (isAdmin()) {
      return true;
    }
    Long userId = getAuthenticatedUserId();
    return userId != null && userId.equals(resourceOwnerId);
  }

  public static boolean canAccessPessoa(Long pessoaId) {
    return isOwnerOrAdmin(pessoaId);
  }
}
