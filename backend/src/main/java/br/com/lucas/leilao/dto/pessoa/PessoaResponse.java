package br.com.lucas.leilao.dto.pessoa;

import java.util.List;
import br.com.lucas.leilao.model.Pessoa;

public record PessoaResponse(
    Long id,
    String nome,
    String email,
    Boolean ativo,
    List<PerfilSimpleResponse> perfis) {

  public static PessoaResponse fromEntity(Pessoa pessoa) {
    List<PerfilSimpleResponse> perfis = pessoa.getPerfis().stream()
        .map(pp -> new PerfilSimpleResponse(
            pp.getPerfil().getId(),
            pp.getPerfil().getTipo().name()))
        .toList();

    return new PessoaResponse(
        pessoa.getId(),
        pessoa.getNome(),
        pessoa.getEmail(),
        pessoa.getAtivo(),
        perfis);
  }

  public record PerfilSimpleResponse(
      Long id,
      String tipo) {
  }
}
