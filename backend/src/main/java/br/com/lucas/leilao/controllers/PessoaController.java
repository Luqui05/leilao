package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.pessoa.PessoaListResponse;
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
  public ResponseEntity<List<PessoaListResponse>> readAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Pessoa> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Pessoa> update(@PathVariable Long id, @Valid @RequestBody PessoaUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
