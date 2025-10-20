package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.imagem.ImagemUpdateRequest;
import br.com.lucas.leilao.model.Imagem;
import br.com.lucas.leilao.services.ImagemService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/imagens")
public class ImagemController {

  @Autowired
  private ImagemService service;

  @PostMapping
  public ResponseEntity<Imagem> create(@Valid @RequestBody Imagem request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<List<Imagem>> readAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Imagem> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Imagem> update(@PathVariable Long id, @Valid @RequestBody ImagemUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}