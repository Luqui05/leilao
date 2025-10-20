package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.lance.LanceUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Lance;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.repositories.LanceRepository;
import br.com.lucas.leilao.repositories.LeilaoRepository;
import br.com.lucas.leilao.repositories.PessoaRepository;

@Service
public class LanceService {

  @Autowired private LanceRepository repository;
  @Autowired private LeilaoRepository leilaoRepository;
  @Autowired private PessoaRepository pessoaRepository;

  @Transactional(readOnly = true)
  public List<Lance> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Lance findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Lance não encontrado! Id: " + id));
  }

  @Transactional
  public Lance save(Lance entity) {
    if (entity.getLeilao() == null || entity.getLeilao().getId() == null) {
      throw new NotFoundException("Leilão não informado");
    }
    if (entity.getAutor() == null || entity.getAutor().getId() == null) {
      throw new NotFoundException("Autor não informado");
    }
    Leilao leilao = leilaoRepository.findById(entity.getLeilao().getId())
        .orElseThrow(() -> new NotFoundException("Leilão não encontrado! Id: " + entity.getLeilao().getId()));
    Pessoa autor = pessoaRepository.findById(entity.getAutor().getId())
        .orElseThrow(() -> new NotFoundException("Pessoa (autor) não encontrada! Id: " + entity.getAutor().getId()));

    entity.setLeilao(leilao);
    entity.setAutor(autor);
    return repository.save(entity);
  }

  @Transactional
  public Lance update(Long id, LanceUpdateRequest req) {
    var entity = findById(id);

    req.valorLance().ifPresent(entity::setValorLance);
    req.dataHora().ifPresent(entity::setDataHora);

    req.leilaoId().ifPresent(leilaoId -> {
      Leilao leilao = leilaoRepository.findById(leilaoId)
          .orElseThrow(() -> new NotFoundException("Leilão não encontrado! Id: " + leilaoId));
      entity.setLeilao(leilao);
    });

    req.autorId().ifPresent(autorId -> {
      Pessoa autor = pessoaRepository.findById(autorId)
          .orElseThrow(() -> new NotFoundException("Pessoa (autor) não encontrada! Id: " + autorId));
      entity.setAutor(autor);
    });

    return repository.save(entity);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}