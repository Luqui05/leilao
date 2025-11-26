package br.com.lucas.leilao.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.leilao.LeilaoCreateRequest;
import br.com.lucas.leilao.dto.leilao.LeilaoResponse;
import br.com.lucas.leilao.dto.leilao.LeilaoUpdateRequest;
import br.com.lucas.leilao.enums.StatusLeilao;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.services.LeilaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leiloes")
public class LeilaoController {

  @Autowired
  private LeilaoService service;

  @PostMapping
  @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
  public ResponseEntity<LeilaoResponse> create(@Valid @RequestBody LeilaoCreateRequest request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Page<LeilaoResponse>> buscar(
      @RequestParam(required = false) StatusLeilao status,
      @RequestParam(required = false) Long categoriaId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
      @RequestParam(required = false) String termo,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id,asc") String[] sort) {

    String sortField = sort.length > 0 ? sort[0] : "id";
    String sortDirection = sort.length > 1 ? sort[1] : "asc";
    Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

    Page<LeilaoResponse> resultado = service.buscarComFiltros(status, categoriaId, dataInicio, dataFim, termo, pageable);
    return ResponseEntity.ok(resultado);
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<LeilaoResponse> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findByIdAsResponse(id));
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
  public ResponseEntity<LeilaoResponse> update(@PathVariable Long id, @Valid @RequestBody LeilaoUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}