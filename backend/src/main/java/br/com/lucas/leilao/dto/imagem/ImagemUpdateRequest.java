package br.com.lucas.leilao.dto.imagem;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.validation.constraints.Size;

public record ImagemUpdateRequest(
  Optional<LocalDateTime> dataHoraCadastro,
  Optional<@Size(max = 255) String> nomeImagem,
  Optional<Long> leilaoId
) {
  public ImagemUpdateRequest {
    dataHoraCadastro = dataHoraCadastro == null ? Optional.empty() : dataHoraCadastro;
    nomeImagem = nomeImagem == null ? Optional.empty() : nomeImagem;
    leilaoId = leilaoId == null ? Optional.empty() : leilaoId;
  }

  public static ImagemUpdateRequest empty() {
    return new ImagemUpdateRequest(Optional.empty(), Optional.empty(), Optional.empty());
  }
}