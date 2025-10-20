package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.pessoaperfil.PessoaPerfilUpdateRequest;
import br.com.lucas.leilao.model.PessoaPerfil;
import br.com.lucas.leilao.services.PessoaPerfilService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pessoas-perfis")
public class PessoaPerfilController {

  @Autowired
  private PessoaPerfilService service;

  @PostMapping
  public ResponseEntity<PessoaPerfil> create(@Valid @RequestBody PessoaPerfil request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<List<PessoaPerfil>> readAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PessoaPerfil> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<PessoaPerfil> update(@PathVariable Long id, @Valid @RequestBody PessoaPerfilUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}