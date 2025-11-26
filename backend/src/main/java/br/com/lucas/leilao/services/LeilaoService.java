package br.com.lucas.leilao.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.leilao.LeilaoCreateRequest;
import br.com.lucas.leilao.dto.leilao.LeilaoResponse;
import br.com.lucas.leilao.dto.leilao.LeilaoUpdateRequest;
import br.com.lucas.leilao.enums.StatusLeilao;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Categoria;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.repositories.CategoriaRepository;
import br.com.lucas.leilao.repositories.LeilaoRepository;
import br.com.lucas.leilao.repositories.PessoaRepository;
import br.com.lucas.leilao.security.SecurityUtils;

@Service
public class LeilaoService {

  @Autowired
  private LeilaoRepository repository;

  @Autowired
  private CategoriaRepository categoriaRepository;

  @Autowired
  private PessoaRepository pessoaRepository;

  @Transactional(readOnly = true)
  public Page<LeilaoResponse> buscarComFiltros(
      StatusLeilao status,
      Long categoriaId,
      LocalDateTime dataInicio,
      LocalDateTime dataFim,
      String termo,
      Pageable pageable) {
    Page<Leilao> leiloes = repository.buscarComFiltros(status, categoriaId, dataInicio, dataFim, termo, pageable);
    return leiloes.map(LeilaoResponse::fromEntity);
  }

  @Transactional(readOnly = true)
  public Leilao findById(Long id) {
    return repository.findById(id)
      .orElseThrow(() -> new NotFoundException("Leilão não encontrado! Id: " + id));
  }

  @Transactional(readOnly = true)
  public LeilaoResponse findByIdAsResponse(Long id) {
    Leilao leilao = findById(id);
    return LeilaoResponse.fromEntity(leilao);
  }

  @Transactional
  public LeilaoResponse save(LeilaoCreateRequest request) {
    // Buscar categoria
    Categoria categoria = categoriaRepository.findById(request.categoriaId())
        .orElseThrow(() -> new NotFoundException("Categoria não encontrada! Id: " + request.categoriaId()));

    // Definir publicador
    Pessoa publicador;
    if (request.publicadorId() != null) {
      // Apenas ADMIN pode definir outro publicador
      if (!SecurityUtils.isAdmin()) {
        throw new AccessDeniedException("Apenas administradores podem definir o publicador.");
      }
      publicador = pessoaRepository.findById(request.publicadorId())
          .orElseThrow(() -> new NotFoundException("Publicador não encontrado! Id: " + request.publicadorId()));
    } else {
      // Usa o usuário autenticado
      publicador = SecurityUtils.getAuthenticatedUser();
      if (publicador == null) {
        throw new AccessDeniedException("Usuário não autenticado.");
      }
    }

    // Criar entidade
    Leilao leilao = Leilao.builder()
        .titulo(request.titulo())
        .descricao(request.descricao())
        .descricaoDetalhada(request.descricaoDetalhada())
        .dataHoraInicio(request.dataHoraInicio())
        .dataHoraFim(request.dataHoraFim())
        .status(request.status())
        .observacao(request.observacao())
        .valorIncremento(request.valorIncremento())
        .lanceMinimo(request.lanceMinimo())
        .categoria(categoria)
        .publicador(publicador)
        .build();

    Leilao salvo = repository.save(leilao);
    return LeilaoResponse.fromEntity(salvo);
  }

  @Transactional
  public LeilaoResponse update(Long id, LeilaoUpdateRequest req) {
    var leilao = findById(id);
    
    // Validação: apenas o dono ou ADMIN podem editar
    if (!SecurityUtils.isOwnerOrAdmin(leilao.getPublicador().getId())) {
      throw new AccessDeniedException("Você não tem permissão para editar este leilão.");
    }

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
      leilao.setCategoria(cat);
    });

    req.publicadorId().ifPresent(pubId -> {
      // Apenas ADMIN pode alterar o publicador
      if (!SecurityUtils.isAdmin()) {
        throw new AccessDeniedException("Apenas administradores podem alterar o publicador.");
      }
      Pessoa pub = pessoaRepository.findById(pubId)
        .orElseThrow(() -> new NotFoundException("Pessoa (publicador) não encontrada! Id: " + pubId));
      leilao.setPublicador(pub);
    });

    Leilao atualizado = repository.save(leilao);
    return LeilaoResponse.fromEntity(atualizado);
  }

  @Transactional
  public void delete(Long id) {
    var leilao = findById(id);
    
    // Validação: apenas o dono ou ADMIN podem excluir
    if (!SecurityUtils.isOwnerOrAdmin(leilao.getPublicador().getId())) {
      throw new AccessDeniedException("Você não tem permissão para excluir este leilão.");
    }
    
    repository.deleteById(id);
  }
}