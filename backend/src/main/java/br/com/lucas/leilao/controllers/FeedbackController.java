package br.com.lucas.leilao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.lucas.leilao.dto.feedback.FeedbackUpdateRequest;
import br.com.lucas.leilao.model.Feedback;
import br.com.lucas.leilao.services.FeedbackService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

  @Autowired
  private FeedbackService service;

  @PostMapping
  public ResponseEntity<Feedback> create(@Valid @RequestBody Feedback request) {
    var response = service.save(request);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<List<Feedback>> readAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Feedback> readById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Feedback> update(@PathVariable Long id, @Valid @RequestBody FeedbackUpdateRequest request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}