package br.com.lucas.leilao.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PagamentoUpdateRequest(
  Optional<@Positive BigDecimal> valor,
  Optional<LocalDateTime> dataHora,
  Optional<@Size(max = 40) String> status,
  Optional<Long> leilaoId
) {
  public PagamentoUpdateRequest {
    valor = valor == null ? Optional.empty() : valor;
    dataHora = dataHora == null ? Optional.empty() : dataHora;
    status = status == null ? Optional.empty() : status;
    leilaoId = leilaoId == null ? Optional.empty() : leilaoId;
  }

  public static PagamentoUpdateRequest empty() {
    return new PagamentoUpdateRequest(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }
}