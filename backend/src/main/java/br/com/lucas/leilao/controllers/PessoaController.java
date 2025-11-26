package br.com.lucas.leilao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucas.leilao.dto.pessoa.PessoaResponse;
import br.com.lucas.leilao.dto.pessoa.PessoaUpdateRequest;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.services.PessoaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

  @Autowired
  private PessoaService service;

  @PostMapping
  public ResponseEntity<Pessoa> create(@Valid @RequestBody Pessoa request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<PessoaResponse>> buscar(
      @RequestParam(required = false) String termo,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id,asc") String[] sort) {

    String sortField = sort.length > 0 ? sort[0] : "id";
    String sortDirection = sort.length > 1 ? sort[1] : "asc";
    Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

    Page<PessoaResponse> resultado = service.buscarComFiltros(termo, pageable);
    return ResponseEntity.ok(resultado);
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<PessoaResponse> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findByIdAsResponse(id));
  }

  @PatchMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<PessoaResponse> update(@PathVariable Long id, @Valid @RequestBody PessoaUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
