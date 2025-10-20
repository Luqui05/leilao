package br.com.lucas.leilao.dto.pessoa;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Update parcial de Pessoa via PATCH.
 * Mantém o padrão de UpdateRequest com Optional.
 */
public record PessoaUpdateRequest(
    Optional<@Size(min = 1, max = 150) String> nome,
    Optional<@Email @Size(max = 180) String> email,
    Optional<@Size(min = 6, max = 255) String> senha,
    Optional<@Size(max = 120) String> codigoValidacao,
    Optional<LocalDateTime> validadeCodigoValidacao,
    Optional<Boolean> ativo,
    Optional<byte[]> fotoPerfil
) {
  public PessoaUpdateRequest {
    nome = nome == null ? Optional.empty() : nome;
    email = email == null ? Optional.empty() : email;
    senha = senha == null ? Optional.empty() : senha;
    codigoValidacao = codigoValidacao == null ? Optional.empty() : codigoValidacao;
    validadeCodigoValidacao = validadeCodigoValidacao == null ? Optional.empty() : validadeCodigoValidacao;
    ativo = ativo == null ? Optional.empty() : ativo;
    fotoPerfil = fotoPerfil == null ? Optional.empty() : fotoPerfil;
  }

  public static PessoaUpdateRequest empty() {
    return new PessoaUpdateRequest(Optional.empty(), Optional.empty(), Optional.empty(),
        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }
}
