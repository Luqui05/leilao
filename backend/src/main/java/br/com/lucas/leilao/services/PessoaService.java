package br.com.lucas.leilao.services;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.leilao.dto.auth.PasswordChangeRequest;
import br.com.lucas.leilao.dto.auth.PasswordChangeWithCodeRequest;
import br.com.lucas.leilao.dto.pessoa.PessoaResponse;
import br.com.lucas.leilao.dto.pessoa.PessoaUpdateRequest;
import br.com.lucas.leilao.enums.TipoPerfil;
import br.com.lucas.leilao.exceptions.NotFoundException;
import br.com.lucas.leilao.model.Perfil;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.model.PessoaPerfil;
import br.com.lucas.leilao.repositories.PerfilRepository;
import br.com.lucas.leilao.repositories.PessoaRepository;
import br.com.lucas.leilao.security.SecurityUtils;

@Service
public class PessoaService {

  @Autowired
  private PessoaRepository repository;

  @Autowired
  private PerfilRepository perfilRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

  @Transactional(readOnly = true)
  public Page<PessoaResponse> buscarComFiltros(String termo, Pageable pageable) {
    Page<Pessoa> pessoas = repository.buscarComFiltros(termo, pageable);
    return pessoas.map(PessoaResponse::fromEntity);
  }

  @Transactional(readOnly = true)
  public Pessoa findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Pessoa não encontrada! Id: " + id));
  }

  @Transactional(readOnly = true)
  public PessoaResponse findByIdAsResponse(Long id) {
    // Validação: só pode acessar a si mesmo ou ser ADMIN
    if (!SecurityUtils.canAccessPessoa(id)) {
      throw new AccessDeniedException("Você não tem permissão para acessar estes dados.");
    }
    
    Pessoa pessoa = findById(id);
    return PessoaResponse.fromEntity(pessoa);
  }

  @Transactional
  public Pessoa save(Pessoa pessoa) {
    pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));

    boolean isFirstUser = repository.count() == 0;
    TipoPerfil tipoPerfil = isFirstUser ? TipoPerfil.ADMIN : TipoPerfil.COMPRADOR;

    Perfil perfil = perfilRepository.findByTipo(tipoPerfil)
        .orElseThrow(() -> new NotFoundException("Perfil " + tipoPerfil + " não encontrado."));

    PessoaPerfil pessoaPerfil = PessoaPerfil.builder()
        .pessoa(pessoa)
        .perfil(perfil)
        .build();

    pessoa.getPerfis().add(pessoaPerfil);
    Pessoa pessoaSalva = repository.save(pessoa);
    emailService.enviarEmailConfirmacaoCadastro(pessoaSalva);
    return pessoaSalva;
  }

  @Transactional
  public PessoaResponse update(Long id, PessoaUpdateRequest req) {
    // Validação: só pode editar a si mesmo ou ser ADMIN
    if (!SecurityUtils.canAccessPessoa(id)) {
      throw new AccessDeniedException("Você não tem permissão para editar estes dados.");
    }

    var pessoa = findById(id);
    req.nome().ifPresent(pessoa::setNome);
    req.email().ifPresent(pessoa::setEmail);
    req.senha().ifPresent(senha -> pessoa.setSenha(passwordEncoder.encode(senha)));
    
    // Apenas ADMIN pode alterar ativo
    req.ativo().ifPresent(ativo -> {
      if (!SecurityUtils.isAdmin()) {
        throw new AccessDeniedException("Apenas administradores podem alterar o status ativo.");
      }
      pessoa.setAtivo(ativo);
    });
    
    Pessoa atualizado = repository.save(pessoa);
    return PessoaResponse.fromEntity(atualizado);
  }

  @Transactional
  public void delete(Long id) {
    // Apenas ADMIN pode excluir (já protegido no SecurityConfig)
    repository.deleteById(id);
  }

  @Transactional
  public void solicitarRecuperacaoSenha(String email) {
    repository.findByEmail(email).ifPresent(pessoa -> {
      String codigo = String.format("%06d", ThreadLocalRandom.current().nextInt(100000, 1000000));
      pessoa.setCodigoValidacao(codigo);
      pessoa.setValidadeCodigoValidacao(LocalDateTime.now().plusMinutes(10));
      repository.save(pessoa);
      emailService.enviarEmailRecuperacaoSenha(pessoa);
    });
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

    pessoa.setSenha(passwordEncoder.encode(request.novaSenha()));
    pessoa.setCodigoValidacao(null);
    pessoa.setValidadeCodigoValidacao(null);
    repository.save(pessoa);
  }

  @Transactional
  public void alterarSenhaAutenticado(String email, PasswordChangeRequest request) {
    Pessoa pessoa = repository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

    if (!passwordEncoder.matches(request.senhaAtual(), pessoa.getPassword())) {
      throw new BadCredentialsException("A senha atual está incorreta");
    }

    pessoa.setSenha(passwordEncoder.encode(request.novaSenha()));
    repository.save(pessoa);
  }
}
