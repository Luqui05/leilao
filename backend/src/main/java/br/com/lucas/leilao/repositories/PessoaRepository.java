package br.com.lucas.leilao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
  Optional<Pessoa> findByEmail(String email);
}
