package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.imagem.ImagemUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Imagem;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.repositories.ImagemRepository;
import br.com.lucas.leilao.repositories.LeilaoRepository;

@Service
public class ImagemService {

  @Autowired private ImagemRepository repository;
  @Autowired private LeilaoRepository leilaoRepository;

  @Transactional(readOnly = true)
  public List<Imagem> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Imagem findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Imagem não encontrada! Id: " + id));
  }

  @Transactional
  public Imagem save(Imagem entity) {
    if (entity.getLeilao() == null || entity.getLeilao().getId() == null) {
      throw new NotFoundException("Leilão não informado");
    }
    Leilao leilao = leilaoRepository.findById(entity.getLeilao().getId())
        .orElseThrow(() -> new NotFoundException("Leilão não encontrado! Id: " + entity.getLeilao().getId()));
    entity.setLeilao(leilao);
    return repository.save(entity);
  }

  @Transactional
  public Imagem update(Long id, ImagemUpdateRequest req) {
    var entity = findById(id);

    req.dataHoraCadastro().ifPresent(entity::setDataHoraCadastro);
    req.nomeImagem().ifPresent(entity::setNomeImagem);

    req.leilaoId().ifPresent(leilaoId -> {
      Leilao leilao = leilaoRepository.findById(leilaoId)
          .orElseThrow(() -> new NotFoundException("Leilão não encontrado! Id: " + leilaoId));
      entity.setLeilao(leilao);
    });

    return repository.save(entity);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}