package br.com.lucas.leilao.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lucas.leilao.enums.StatusLeilao;
import br.com.lucas.leilao.model.Leilao;

public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

  @Query("SELECT l FROM Leilao l WHERE " +
      "(:status IS NULL OR l.status = :status) AND " +
      "(:categoriaId IS NULL OR l.categoria.id = :categoriaId) AND " +
      "(:dataInicio IS NULL OR l.dataHoraInicio >= :dataInicio) AND " +
      "(:dataFim IS NULL OR l.dataHoraFim <= :dataFim) AND " +
      "(:termo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
      "LOWER(l.descricao) LIKE LOWER(CONCAT('%', :termo, '%')))")
  Page<Leilao> buscarComFiltros(
      @Param("status") StatusLeilao status,
      @Param("categoriaId") Long categoriaId,
      @Param("dataInicio") LocalDateTime dataInicio,
      @Param("dataFim") LocalDateTime dataFim,
      @Param("termo") String termo,
      Pageable pageable);
}