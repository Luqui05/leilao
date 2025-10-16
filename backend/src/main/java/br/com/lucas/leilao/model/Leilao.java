package br.com.lucas.leilao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.lucas.leilao.enums.StatusLeilao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leiloes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Leilao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @NotBlank
  @Size(max = 160)
  private String titulo;

  @NotBlank
  @Size(max = 500)
  private String descricao;

  @Size(max = 2000)
  private String descricaoDetalhada;

  @NotNull
  private LocalDateTime dataHoraInicio;

  @NotNull
  private LocalDateTime dataHoraFim;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private StatusLeilao status;

  @Size(max = 500)
  private String observacao;

  @NotNull
  @Positive
  @Column(precision = 15, scale = 2, nullable = false)
  private BigDecimal valorIncremento;

  @NotNull
  @Positive
  @Column(precision = 15, scale = 2, nullable = false)
  private BigDecimal lanceMinimo;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "categoria_id", nullable = false, foreignKey = @ForeignKey(name = "fk_leilao_categoria"))
  private Categoria categoria;

  // publicador do leilão
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "publicador_id", nullable = false, foreignKey = @ForeignKey(name = "fk_leilao_publicador"))
  private Pessoa publicador;

  @OneToMany(mappedBy = "leilao", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Imagem> imagens = new ArrayList<>();

  @OneToMany(mappedBy = "leilao", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Lance> lances = new ArrayList<>();

  @OneToOne(mappedBy = "leilao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Pagamento pagamento;
}
