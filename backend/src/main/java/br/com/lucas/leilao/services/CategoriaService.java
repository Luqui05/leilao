package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Categoria;
import br.com.lucas.leilao.repositories.CategoriaRepository;
import br.com.lucas.leilao.dto.categoria.CategoriaUpdateRequest;

@Service
public class CategoriaService {

  @Autowired
  private CategoriaRepository repository;

  @Transactional(readOnly = true)
  public List<Categoria> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Categoria findById(Long id) {
    return repository.findById(id).orElseThrow(
        () -> new NotFoundException("Categoria não encontrada! Id: " + id));
  }

  @Transactional
  public Categoria save(Categoria categoria) {
    return repository.save(categoria);
  }

  @Transactional
  public Categoria update(Long id, CategoriaUpdateRequest req) {
    var categoria = findById(id);
    // req já vem normalizado (Optionals nunca null)
    req.nome().ifPresent(categoria::setNome);
    req.observacao().ifPresent(categoria::setObservacao);
    return repository.save(categoria);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}
