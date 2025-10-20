package br.com.lucas.leilao.dto.pessoaperfil;

import java.util.Optional;

public record PessoaPerfilUpdateRequest(
  Optional<Long> pessoaId,
  Optional<Long> perfilId
) {
  public PessoaPerfilUpdateRequest {
    pessoaId = pessoaId == null ? Optional.empty() : pessoaId;
    perfilId = perfilId == null ? Optional.empty() : perfilId;
  }

  public static PessoaPerfilUpdateRequest empty() {
    return new PessoaPerfilUpdateRequest(Optional.empty(), Optional.empty());
  }
}