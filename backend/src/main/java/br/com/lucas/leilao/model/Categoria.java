package br.com.lucas.leilao.model;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @NotBlank
  @Size(max = 120)
  private String nome;

  @Size(max = 500)
  private String observacao;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "criador_id", nullable = false, foreignKey = @ForeignKey(name = "fk_caategoria_criador"))
  private Pessoa criador;

  @OneToMany(mappedBy = "categorias")
  @Builder.Default
  private Set<Leilao> leiloes = new LinkedHashSet<>();
}
