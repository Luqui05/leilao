package br.com.lucas.leilao.dto.lance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.validation.constraints.Positive;

public record LanceUpdateRequest(
  Optional<@Positive BigDecimal> valorLance,
  Optional<LocalDateTime> dataHora,
  Optional<Long> leilaoId,
  Optional<Long> autorId
) {
  public LanceUpdateRequest {
    valorLance = valorLance == null ? Optional.empty() : valorLance;
    dataHora = dataHora == null ? Optional.empty() : dataHora;
    leilaoId = leilaoId == null ? Optional.empty() : leilaoId;
    autorId = autorId == null ? Optional.empty() : autorId;
  }

  public static LanceUpdateRequest empty() {
    return new LanceUpdateRequest(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }
}