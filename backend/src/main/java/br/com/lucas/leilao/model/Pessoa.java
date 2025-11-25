package br.com.lucas.leilao.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.lucas.leilao.validation.ValidPassword;
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
public class Pessoa implements UserDetails {

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
  @ValidPassword
  @Size(max = 255)
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
  @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @Builder.Default
  private List<PessoaPerfil> perfis = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return perfis.stream()
        .map(pessoaPerfil -> new SimpleGrantedAuthority("ROLE_" + pessoaPerfil.getPerfil().getTipo().name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return this.senha;
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
