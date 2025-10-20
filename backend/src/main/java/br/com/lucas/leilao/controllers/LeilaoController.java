package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.leilao.LeilaoUpdateRequest;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.services.LeilaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leiloes")
public class LeilaoController {

  @Autowired
  private LeilaoService service;

  @PostMapping
  public ResponseEntity<Leilao> create(@Valid @RequestBody Leilao request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<List<Leilao>> readAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Leilao> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Leilao> update(@PathVariable Long id, @Valid @RequestBody LeilaoUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}