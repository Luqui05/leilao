package br.com.lucas.leilao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.enums.TipoPerfil;
import br.com.lucas.leilao.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
  Optional<Perfil> findByTipo(TipoPerfil tipo);
}
