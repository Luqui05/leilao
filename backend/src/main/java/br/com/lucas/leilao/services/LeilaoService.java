package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.leilao.LeilaoUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Categoria;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.repositories.CategoriaRepository;
import br.com.lucas.leilao.repositories.LeilaoRepository;
import br.com.lucas.leilao.repositories.PessoaRepository;

@Service
public class LeilaoService {

  @Autowired
  private LeilaoRepository repository;

  @Autowired
  private CategoriaRepository categoriaRepository;

  @Autowired
  private PessoaRepository pessoaRepository;

  @Transactional(readOnly = true)
  public List<Leilao> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Leilao findById(Long id) {
    return repository.findById(id)
      .orElseThrow(() -> new NotFoundException("Leilão não encontrado! Id: " + id));
  }

  @Transactional
  public Leilao save(Leilao leilao) {
    return repository.save(leilao);
  }

  @Transactional
  public Leilao update(Long id, LeilaoUpdateRequest req) {
    var leilao = findById(id);

    req.titulo().ifPresent(leilao::setTitulo);
    req.descricao().ifPresent(leilao::setDescricao);
    req.descricaoDetalhada().ifPresent(leilao::setDescricaoDetalhada);
    req.dataHoraInicio().ifPresent(leilao::setDataHoraInicio);
    req.dataHoraFim().ifPresent(leilao::setDataHoraFim);
    req.status().ifPresent(leilao::setStatus);
    req.observacao().ifPresent(leilao::setObservacao);
    req.valorIncremento().ifPresent(leilao::setValorIncremento);
    req.lanceMinimo().ifPresent(leilao::setLanceMinimo);

    req.categoriaId().ifPresent(catId -> {
      Categoria cat = categoriaRepository.findById(catId)
        .orElseThrow(() -> new NotFoundException("Categoria não encontrada! Id: " + catId));
      // campo se chama "categorias" no model
      leilao.setCategoria(cat);
    });

    req.publicadorId().ifPresent(pubId -> {
      Pessoa pub = pessoaRepository.findById(pubId)
        .orElseThrow(() -> new NotFoundException("Pessoa (publicador) não encontrada! Id: " + pubId));
      leilao.setPublicador(pub);
    });

    return repository.save(leilao);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}