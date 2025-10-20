package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.perfil.PerfilUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Perfil;
import br.com.lucas.leilao.repositories.PerfilRepository;

@Service
public class PerfilService {

  @Autowired
  private PerfilRepository repository;

  @Transactional(readOnly = true)
  public List<Perfil> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Perfil findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Perfil não encontrado! Id: " + id));
  }

  @Transactional
  public Perfil save(Perfil perfil) {
    return repository.save(perfil);
  }

  @Transactional
  public Perfil update(Long id, PerfilUpdateRequest req) {
    var perfil = findById(id);
    req.tipo().ifPresent(perfil::setTipo);
    return repository.save(perfil);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}
