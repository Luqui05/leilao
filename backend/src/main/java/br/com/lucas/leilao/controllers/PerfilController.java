package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.perfil.PerfilUpdateRequest;
import br.com.lucas.leilao.model.Perfil;
import br.com.lucas.leilao.services.PerfilService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

  @Autowired
  private PerfilService service;

  @PostMapping
  public ResponseEntity<Perfil> create(@Valid @RequestBody Perfil request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<List<Perfil>> readAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Perfil> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Perfil> update(@PathVariable Long id, @Valid @RequestBody PerfilUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
