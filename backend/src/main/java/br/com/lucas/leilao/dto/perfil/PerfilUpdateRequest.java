package br.com.lucas.leilao.dto.perfil;

import java.util.Optional;

import br.com.lucas.leilao.enums.TipoPerfil;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para atualização parcial de Perfil.
 */
public record PerfilUpdateRequest(
    Optional<@NotNull TipoPerfil> tipo
) {
  public PerfilUpdateRequest {
    tipo = tipo == null ? Optional.empty() : tipo;
  }

  public static PerfilUpdateRequest empty() {
    return new PerfilUpdateRequest(Optional.empty());
  }
}
