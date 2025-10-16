package br.com.lucas.leilao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoas_perfis", uniqueConstraints = {
    @UniqueConstraint(name = "uk_pessoa_perfil", columnNames = { "pessoa_id", "perfil_id" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PessoaPerfil {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pessoa_perfil_pessoa"))
  private Pessoa pessoa;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "perfil_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pessoa_perfil_perfil"))
  private Perfil perfil;
}
