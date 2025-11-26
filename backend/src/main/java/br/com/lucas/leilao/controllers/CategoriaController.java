package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.lucas.leilao.dto.categoria.CategoriaUpdateRequest;
import br.com.lucas.leilao.model.Categoria;
import br.com.lucas.leilao.services.CategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

  @Autowired
  private CategoriaService service;

  @PostMapping
  public ResponseEntity<Categoria> create(@Valid @RequestBody Categoria request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<Page<Categoria>> buscar(
      @RequestParam(required = false) String termo,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id,asc") String[] sort) {
    String sortField = sort.length > 0 ? sort[0] : "id";
    String sortDirection = sort.length > 1 ? sort[1] : "asc";
    Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

    Page<Categoria> resultado = service.buscarComFiltrosPage(termo, pageable);
    return ResponseEntity.ok(resultado);
  }

  @GetMapping("/todas")
  public ResponseEntity<List<Categoria>> readAll() {
    List<Categoria> categorias = service.findAll();
    return ResponseEntity.ok().body(categorias);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Categoria> readById(@PathVariable("id") Long id) {
    Categoria categoria = service.findById(id);
    return ResponseEntity.ok().body(categoria);
  }

  // Update parcial -> PATCH
  @PatchMapping("/{id}")
  public ResponseEntity<Categoria> update(@PathVariable("id") Long id,
      @Valid @RequestBody CategoriaUpdateRequest request) {
    Categoria categoria = service.update(id, request);
    return ResponseEntity.ok().body(categoria);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
