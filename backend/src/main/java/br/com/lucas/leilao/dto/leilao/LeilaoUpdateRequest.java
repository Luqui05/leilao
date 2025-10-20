package br.com.lucas.leilao.dto.leilao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import br.com.lucas.leilao.enums.StatusLeilao;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LeilaoUpdateRequest(
  Optional<@Size(min = 1, max = 160) String> titulo,
  Optional<@Size(min = 1, max = 500) String> descricao,
  Optional<@Size(max = 2000) String> descricaoDetalhada,
  Optional<LocalDateTime> dataHoraInicio,
  Optional<LocalDateTime> dataHoraFim,
  Optional<StatusLeilao> status,
  Optional<@Size(max = 500) String> observacao,
  Optional<@Positive BigDecimal> valorIncremento,
  Optional<@Positive BigDecimal> lanceMinimo,
  Optional<Long> categoriaId,
  Optional<Long> publicadorId
) {
  public LeilaoUpdateRequest {
    titulo = titulo == null ? Optional.empty() : titulo;
    descricao = descricao == null ? Optional.empty() : descricao;
    descricaoDetalhada = descricaoDetalhada == null ? Optional.empty() : descricaoDetalhada;
    dataHoraInicio = dataHoraInicio == null ? Optional.empty() : dataHoraInicio;
    dataHoraFim = dataHoraFim == null ? Optional.empty() : dataHoraFim;
    status = status == null ? Optional.empty() : status;
    observacao = observacao == null ? Optional.empty() : observacao;
    valorIncremento = valorIncremento == null ? Optional.empty() : valorIncremento;
    lanceMinimo = lanceMinimo == null ? Optional.empty() : lanceMinimo;
    categoriaId = categoriaId == null ? Optional.empty() : categoriaId;
    publicadorId = publicadorId == null ? Optional.empty() : publicadorId;
  }

  public static LeilaoUpdateRequest empty() {
    return new LeilaoUpdateRequest(
      Optional.empty(), Optional.empty(), Optional.empty(),
      Optional.empty(), Optional.empty(), Optional.empty(),
      Optional.empty(), Optional.empty(), Optional.empty(),
      Optional.empty(), Optional.empty()
    );
  }
}