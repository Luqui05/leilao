package br.com.lucas.leilao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "pagamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pagamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @NotNull
  @Positive
  @Column(precision = 15, scale = 2, nullable = false)
  private BigDecimal valor;

  @NotNull
  private LocalDateTime dataHora;

  @NotBlank
  @Column(length = 40, nullable = false)
  private String status;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "leilao_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_pagamento_leilao"))
  private Leilao leilao;
}
