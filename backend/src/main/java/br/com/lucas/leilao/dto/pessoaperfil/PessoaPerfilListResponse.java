package br.com.lucas.leilao.dto.pessoaperfil;

import br.com.lucas.leilao.enums.TipoPerfil;

public record PessoaPerfilListResponse(
    Long id,
    PessoaSimpleDTO pessoa,
    PerfilSimpleDTO perfil
) {
  public record PessoaSimpleDTO(Long id, String nome, String email) {}
  public record PerfilSimpleDTO(Long id, TipoPerfil tipo) {}
}
