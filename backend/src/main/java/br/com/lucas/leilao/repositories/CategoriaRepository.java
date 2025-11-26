package br.com.lucas.leilao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lucas.leilao.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

  @Query("SELECT c FROM Categoria c WHERE " +
      "(:termo IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
      "LOWER(c.observacao) LIKE LOWER(CONCAT('%', :termo, '%')))")
  Page<Categoria> buscarComFiltros(@Param("termo") String termo, Pageable pageable);
}
