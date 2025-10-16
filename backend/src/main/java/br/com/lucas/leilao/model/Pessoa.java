package br.com.lucas.leilao.model;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoas", uniqueConstraints = {
    @UniqueConstraint(name = "uk_pessoa_email", columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pessoa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 150)
  private String nome;

  @NotBlank
  @Email
  @Size(max = 150)
  private String email;

  @NotBlank
  @Size(min = 6, max = 255)
  private String senha;

  @Size(max = 120)
  private String codigoValidacao;

  private LocalDateTime validadeCodigoValidacao;

  @Builder.Default
  private Boolean ativo = Boolean.TRUE;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] fotoPerfil;

  // Pessoa <-> Perfil (classe associativa)
  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<PessoaPerfil> perfis = new LinkedHashSet<>();

  // Pessoa cria Categoria
  @OneToMany(mappedBy = "criador")
  @Builder.Default
  private Set<Categoria> categoriasCriadas = new LinkedHashSet<>();

  // Pessoa publica Leilao
  @OneToMany(mappedBy = "publicador")
  @Builder.Default
  private Set<Leilao> leiloesPublicados = new LinkedHashSet<>();

  // Pessoa realiza Lance
  @OneToMany(mappedBy = "autor")
  @Builder.Default
  private Set<Lance> lances = new LinkedHashSet<>();

  // Pessoa escreve Feedback
  @OneToMany(mappedBy = "autor")
  @Builder.Default
  private Set<Feedback> feedbacksEscritos = new LinkedHashSet<>();

  // Pessoa recebe Feedback (destinatário)
  @OneToMany(mappedBy = "destinatario")
  @Builder.Default
  private Set<Feedback> feedbacksRecebidos = new LinkedHashSet<>();
}
