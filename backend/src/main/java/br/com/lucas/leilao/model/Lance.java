package br.com.lucas.leilao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "lances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Positive
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valorLance;

    @NotNull
    private LocalDateTime dataHora;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "leilao_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_lance_leilao"))
    private Leilao leilao;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_lance_autor"))
    private Pessoa autor;
}
