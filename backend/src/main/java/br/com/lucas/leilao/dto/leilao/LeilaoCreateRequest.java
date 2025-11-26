package br.com.lucas.leilao.dto.leilao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.lucas.leilao.enums.StatusLeilao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LeilaoCreateRequest(
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 160, message = "O título deve ter no máximo 160 caracteres")
    String titulo,

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    String descricao,

    @Size(max = 2000, message = "A descrição detalhada deve ter no máximo 2000 caracteres")
    String descricaoDetalhada,

    @NotNull(message = "A data/hora de início é obrigatória")
    LocalDateTime dataHoraInicio,

    @NotNull(message = "A data/hora de fim é obrigatória")
    LocalDateTime dataHoraFim,

    @NotNull(message = "O status é obrigatório")
    StatusLeilao status,

    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
    String observacao,

    @NotNull(message = "O valor de incremento é obrigatório")
    @Positive(message = "O valor de incremento deve ser positivo")
    BigDecimal valorIncremento,

    @NotNull(message = "O lance mínimo é obrigatório")
    @Positive(message = "O lance mínimo deve ser positivo")
    BigDecimal lanceMinimo,

    @NotNull(message = "A categoria é obrigatória")
    Long categoriaId,

    Long publicadorId
) {
}