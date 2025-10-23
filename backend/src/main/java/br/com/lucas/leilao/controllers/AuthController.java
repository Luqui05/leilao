package br.com.lucas.leilao.controllers;

import java.lang.module.ResolutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucas.leilao.dto.auth.LoginRequest;
import br.com.lucas.leilao.dto.auth.PasswordChangeRequest;
import br.com.lucas.leilao.dto.auth.PasswordChangeWithCodeRequest;
import br.com.lucas.leilao.dto.auth.PasswordRecover;
import br.com.lucas.leilao.dto.auth.TokenResponse;
import br.com.lucas.leilao.model.Pessoa;
import br.com.lucas.leilao.security.JwtTokenProvider;
import br.com.lucas.leilao.services.PessoaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private PessoaService pessoaService;

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

    String token = jwtTokenProvider.gerarToken(request.email());
    return ResponseEntity.ok(new TokenResponse(token));
  }

  @PostMapping("/recover")
  public ResponseEntity<Void> recoverPassword(@RequestBody @Valid PasswordRecover request) {
    pessoaService.solicitarRecuperacaoSenha(request.email());
    // Retorna 204 No Content para não revelar se o e-mail existe ou não
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/change-password-with-code")
  public ResponseEntity<String> changePasswordWithCode(@RequestBody @Valid PasswordChangeWithCodeRequest request) {
    pessoaService.alterarSenhaComCodigo(request);
    return ResponseEntity.ok("Senha alterada com sucesso.");
  }

  @PostMapping("/change-password")
  public ResponseEntity<String> changePasswordAuthenticated(
      @RequestBody @Valid PasswordChangeRequest request,
      Authentication authentication) {
    Pessoa pessoaLogada = (Pessoa) authentication.getPrincipal();
    pessoaService.alterarSenhaAutenticado(pessoaLogada.getEmail(), request);
    return ResponseEntity.ok("Senha alterada com sucesso.");
  }
}