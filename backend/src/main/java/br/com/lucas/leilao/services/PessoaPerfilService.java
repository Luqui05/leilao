package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.pessoaperfil.PessoaPerfilUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.model.Perfil;
import br.com.lucas.leilao.model.PessoaPerfil;
import br.com.lucas.leilao.repositories.PessoaPerfilRepository;
import br.com.lucas.leilao.repositories.PessoaRepository;
import br.com.lucas.leilao.repositories.PerfilRepository;

@Service
public class PessoaPerfilService {

  @Autowired private PessoaPerfilRepository repository;
  @Autowired private PessoaRepository pessoaRepository;
  @Autowired private PerfilRepository perfilRepository;

  @Transactional(readOnly = true)
  public List<PessoaPerfil> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public PessoaPerfil findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("PessoaPerfil não encontrado! Id: " + id));
  }

  @Transactional
  public PessoaPerfil save(PessoaPerfil entity) {
    if (entity.getPessoa() == null || entity.getPessoa().getId() == null) {
      throw new NotFoundException("Pessoa não informada");
    }
    if (entity.getPerfil() == null || entity.getPerfil().getId() == null) {
      throw new NotFoundException("Perfil não informado");
    }
    Pessoa pessoa = pessoaRepository.findById(entity.getPessoa().getId())
        .orElseThrow(() -> new NotFoundException("Pessoa não encontrada! Id: " + entity.getPessoa().getId()));
    Perfil perfil = perfilRepository.findById(entity.getPerfil().getId())
        .orElseThrow(() -> new NotFoundException("Perfil não encontrado! Id: " + entity.getPerfil().getId()));

    entity.setPessoa(pessoa);
    entity.setPerfil(perfil);
    return repository.save(entity);
  }

  @Transactional
  public PessoaPerfil update(Long id, PessoaPerfilUpdateRequest req) {
    var entity = findById(id);

    req.pessoaId().ifPresent(pid -> {
      Pessoa p = pessoaRepository.findById(pid)
          .orElseThrow(() -> new NotFoundException("Pessoa não encontrada! Id: " + pid));
      entity.setPessoa(p);
    });

    req.perfilId().ifPresent(perfId -> {
      Perfil pf = perfilRepository.findById(perfId)
          .orElseThrow(() -> new NotFoundException("Perfil não encontrado! Id: " + perfId));
      entity.setPerfil(pf);
    });

    return repository.save(entity);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}