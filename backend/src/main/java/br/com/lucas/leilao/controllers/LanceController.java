package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.lance.LanceUpdateRequest;
import br.com.lucas.leilao.model.Lance;
import br.com.lucas.leilao.services.LanceService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lances")
public class LanceController {

  @Autowired
  private LanceService service;

  @PostMapping
  public ResponseEntity<Lance> create(@Valid @RequestBody Lance request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<List<Lance>> readAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Lance> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Lance> update(@PathVariable Long id, @Valid @RequestBody LanceUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}