package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import br.com.lucas.leilao.dto.pessoaperfil.PessoaPerfilListResponse;
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
  public ResponseEntity<PessoaPerfil> create(@Valid @RequestBody PessoaPerfil request, Authentication authentication) {
    var response = service.createForCaller(request, authentication);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<List<PessoaPerfilListResponse>> readAll(Authentication authentication) {
    // If caller is ADMIN, return all; otherwise return only the permissions of the
    // authenticated user
    if (authentication != null && authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return ResponseEntity.ok(service.findAll());
    }

    if (authentication == null) {
      return ResponseEntity.status(401).build();
    }

    // principal should be Pessoa (UserDetails implementation)
    Object principal = authentication.getPrincipal();
    if (principal instanceof br.com.lucas.leilao.model.Pessoa pessoa) {
      return ResponseEntity.ok(service.findByPessoaId(pessoa.getId()));
    }

    return ResponseEntity.status(403).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<PessoaPerfil> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<PessoaPerfil> update(@PathVariable Long id,
      @Valid @RequestBody PessoaPerfilUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}