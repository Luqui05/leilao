package br.com.lucas.leilao.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lucas.leilao.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
  Optional<Pessoa> findByEmail(String email);

  @Query("SELECT p FROM Pessoa p WHERE " +
      "(:termo IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
      "LOWER(p.email) LIKE LOWER(CONCAT('%', :termo, '%')))")
  Page<Pessoa> buscarComFiltros(@Param("termo") String termo, Pageable pageable);
}
