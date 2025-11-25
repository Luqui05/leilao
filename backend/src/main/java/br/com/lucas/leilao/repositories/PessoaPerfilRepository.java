package br.com.lucas.leilao.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.PessoaPerfil;

public interface PessoaPerfilRepository extends JpaRepository<PessoaPerfil, Long> {
  boolean existsByPessoaIdAndPerfilId(Long pessoaId, Long perfilId);
  Optional<PessoaPerfil> findByPessoaIdAndPerfilId(Long pessoaId, Long perfilId);
  List<PessoaPerfil> findAllByPessoaId(Long pessoaId);
}