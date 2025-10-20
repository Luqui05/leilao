package br.com.lucas.leilao.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.pessoa.PessoaUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.repositories.PessoaRepository;

@Service
public class PessoaService {

  @Autowired
  private PessoaRepository repository;

  @Transactional(readOnly = true)
  public List<Pessoa> findAll() {
    return repository.findAll();
  }

  @Transactional(readOnly = true)
  public Pessoa findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Pessoa não encontrada! Id: " + id));
  }

  @Transactional
  public Pessoa save(Pessoa pessoa) {
    return repository.save(pessoa);
  }

  @Transactional
  public Pessoa update(Long id, PessoaUpdateRequest req) {
    var pessoa = findById(id);
    req.nome().ifPresent(pessoa::setNome);
    req.email().ifPresent(pessoa::setEmail);
    req.senha().ifPresent(pessoa::setSenha);
    req.codigoValidacao().ifPresent(pessoa::setCodigoValidacao);
    req.validadeCodigoValidacao().ifPresent(pessoa::setValidadeCodigoValidacao);
    req.ativo().ifPresent(pessoa::setAtivo);
    req.fotoPerfil().ifPresent(pessoa::setFotoPerfil);
    return repository.save(pessoa);
  }

  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }
}
