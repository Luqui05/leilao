package br.com.lucas.leilao.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.auth.PasswordChangeRequest;
import br.com.lucas.leilao.dto.auth.PasswordChangeWithCodeRequest;
import br.com.lucas.leilao.dto.pessoa.PessoaUpdateRequest;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.repositories.PessoaRepository;

@Service
public class PessoaService {

  @Autowired
  private PessoaRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

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
    // Criptografa a senha antes de salvar
    pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));
    return repository.save(pessoa);
  }

  @Transactional
  public Pessoa update(Long id, PessoaUpdateRequest req) {
    var pessoa = findById(id);
    req.nome().ifPresent(pessoa::setNome);
    req.email().ifPresent(pessoa::setEmail);
    req.senha().ifPresent(senha -> pessoa.setSenha(passwordEncoder.encode(senha)));
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

  @Transactional
  public void solicitarRecuperacaoSenha(String email) {
    repository.findByEmail(email).ifPresent(pessoa -> {
      // gera código numérico de 6 digitos
      String codigo = String.format("%06d", ThreadLocalRandom.current().nextInt(100000, 1000000));

      pessoa.setCodigoValidacao(codigo);
      // validade do código -> 10 min
      pessoa.setValidadeCodigoValidacao(LocalDateTime.now().plusMinutes(10));

      repository.save(pessoa);

      // TODO: Futuramente, aqui será o local para chamar o serviço de envio de e-mail
      // emailService.enviarEmailRecuperacao(email, codigo);
    });
    // Se o e-mail não existir, não fazemos nada para evitar ataques de enumeração
    // de usuário.
  }

  @Transactional
  public void alterarSenhaComCodigo(PasswordChangeWithCodeRequest request) {
    Pessoa pessoa = repository.findByEmail(request.email())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    if (pessoa.getCodigoValidacao() == null || !pessoa.getCodigoValidacao().equals(request.codigo())) {
      throw new BadCredentialsException("Código de verificação inválido.");
    }

    if (pessoa.getValidadeCodigoValidacao().isBefore(LocalDateTime.now())) {
      throw new BadCredentialsException("Código de verificação expirado.");
    }

    // altera senha e limpa os campos de recuperação
    pessoa.setSenha(passwordEncoder.encode(request.novaSenha()));
    pessoa.setCodigoValidacao(null);
    pessoa.setValidadeCodigoValidacao(null);

    repository.save(pessoa);
  }

  @Transactional
  public void alterarSenhaAutenticado(String email, PasswordChangeRequest request) {
    Pessoa pessoa = repository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

    // checa se a senha atual fornecida corresponde à senha armazenada
    if (!passwordEncoder.matches(request.senhaAtual(), pessoa.getPassword())) {
      throw new BadCredentialsException("A senha atual está incorreta");
    }

    // criptografa e define a nova senha
    pessoa.setSenha(passwordEncoder.encode(request.novaSenha()));
    repository.save(pessoa);
  }
}
