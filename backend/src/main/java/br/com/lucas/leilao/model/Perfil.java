package br.com.lucas.leilao.model;

import java.util.LinkedHashSet;
import java.util.Set;

import br.com.lucas.leilao.enums.TipoPerfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "perfis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Perfil {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private TipoPerfil tipo;

  // classe associativa
  @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<PessoaPerfil> pessoas = new LinkedHashSet<>();
}
