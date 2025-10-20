package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @GetMapping()
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
  public ResponseEntity<Categoria> update(@PathVariable("id") Long id, @Valid @RequestBody CategoriaUpdateRequest request) {
    Categoria categoria = service.update(id, request);
    return ResponseEntity.ok().body(categoria);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
