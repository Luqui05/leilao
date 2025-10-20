package br.com.lucas.leilao.dto.feedback;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record FeedbackUpdateRequest(
  Optional<@Size(min = 1, max = 1000) String> comentario,
  Optional<@Min(1) @Max(5) Integer> nota,
  Optional<LocalDateTime> dataHora,
  Optional<Long> autorId,
  Optional<Long> destinatarioId
) {
  public FeedbackUpdateRequest {
    comentario = comentario == null ? Optional.empty() : comentario;
    nota = nota == null ? Optional.empty() : nota;
    dataHora = dataHora == null ? Optional.empty() : dataHora;
    autorId = autorId == null ? Optional.empty() : autorId;
    destinatarioId = destinatarioId == null ? Optional.empty() : destinatarioId;
  }

  public static FeedbackUpdateRequest empty() {
    return new FeedbackUpdateRequest(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }
}