package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.pagamento.PagamentoUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.model.Pagamento;
import br.com.lucas.leilao.repositories.LeilaoRepository;
import br.com.lucas.leilao.repositories.PagamentoRepository;

@Service
public class PagamentoService {

  @Autowired private PagamentoRepository repository;
  @Autowired private LeilaoRepository leilaoRepository;

  @Transactional(readOnly = true)
  public List<Pagamento> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Pagamento findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Pagamento não encontrado! Id: " + id));
  }

  @Transactional
  public Pagamento save(Pagamento entity) {
    if (entity.getLeilao() == null || entity.getLeilao().getId() == null) {
      throw new NotFoundException("Leilão não informado");
    }
    Leilao leilao = leilaoRepository.findById(entity.getLeilao().getId())
        .orElseThrow(() -> new NotFoundException("Leilão não encontrado! Id: " + entity.getLeilao().getId()));
    entity.setLeilao(leilao);
    return repository.save(entity);
  }

  @Transactional
  public Pagamento update(Long id, PagamentoUpdateRequest req) {
    var entity = findById(id);

    req.valor().ifPresent(entity::setValor);
    req.dataHora().ifPresent(entity::setDataHora);
    req.status().ifPresent(entity::setStatus);

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