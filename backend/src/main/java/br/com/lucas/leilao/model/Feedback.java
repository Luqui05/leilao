package br.com.lucas.leilao.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(max = 1000)
    private String comentario;

    @NotNull
    @Min(1) @Max(5)
    private Integer nota;

    @NotNull
    private LocalDateTime dataHora;

    // autor do feedback
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_feedback_autor"))
    private Pessoa autor;

    // destinatário do feedback
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_feedback_destinatario"))
    private Pessoa destinatario;
}
