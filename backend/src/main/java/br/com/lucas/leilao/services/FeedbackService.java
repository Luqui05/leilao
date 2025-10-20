package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.feedback.FeedbackUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Feedback;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.repositories.FeedbackRepository;
import br.com.lucas.leilao.repositories.PessoaRepository;

@Service
public class FeedbackService {

  @Autowired private FeedbackRepository repository;
  @Autowired private PessoaRepository pessoaRepository;

  @Transactional(readOnly = true)
  public List<Feedback> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Feedback findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Feedback não encontrado! Id: " + id));
  }

  @Transactional
  public Feedback save(Feedback entity) {
    if (entity.getAutor() == null || entity.getAutor().getId() == null) {
      throw new NotFoundException("Autor não informado");
    }
    if (entity.getDestinatario() == null || entity.getDestinatario().getId() == null) {
      throw new NotFoundException("Destinatário não informado");
    }
    Pessoa autor = pessoaRepository.findById(entity.getAutor().getId())
        .orElseThrow(() -> new NotFoundException("Pessoa (autor) não encontrada! Id: " + entity.getAutor().getId()));
    Pessoa dest = pessoaRepository.findById(entity.getDestinatario().getId())
        .orElseThrow(() -> new NotFoundException("Pessoa (destinatário) não encontrada! Id: " + entity.getDestinatario().getId()));

    entity.setAutor(autor);
    entity.setDestinatario(dest);
    return repository.save(entity);
  }

  @Transactional
  public Feedback update(Long id, FeedbackUpdateRequest req) {
    var entity = findById(id);

    req.comentario().ifPresent(entity::setComentario);
    req.nota().ifPresent(entity::setNota);
    req.dataHora().ifPresent(entity::setDataHora);

    req.autorId().ifPresent(autorId -> {
      Pessoa autor = pessoaRepository.findById(autorId)
          .orElseThrow(() -> new NotFoundException("Pessoa (autor) não encontrada! Id: " + autorId));
      entity.setAutor(autor);
    });

    req.destinatarioId().ifPresent(destId -> {
      Pessoa dest = pessoaRepository.findById(destId)
          .orElseThrow(() -> new NotFoundException("Pessoa (destinatário) não encontrada! Id: " + destId));
      entity.setDestinatario(dest);
    });

    return repository.save(entity);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}