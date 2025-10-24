package br.com.lucas.leilao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import br.com.lucas.leilao.model.Pessoa;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Value("${spring.mail.username}")
  private String mailUsername;

  @Value("${spring.mail.from}")
  private String mailFrom;

  @Value("${app.url}")
  private String appUrl;

  public void enviarEmailRecuperacaoSenha(Pessoa pessoa) {
    final String assunto = "Recuperação de Senha - Leilão Online";
    final String template = "password-recover";

    // variáveis para o template
    Context context = new Context();
    context.setVariable("nome", pessoa.getNome());
    context.setVariable("codigo", pessoa.getCodigoValidacao());

    String link = String.format("%s/alterar-senha?email=%s&code=%s", appUrl, pessoa.getEmail(), pessoa.getCodigoValidacao());

    context.setVariable("link", link);
    enviarEmailComTemplate(pessoa.getEmail(), assunto, template, context);
  }

  public void enviarEmailConfirmacaoCadastro(Pessoa pessoa) {
    final String assunto = "Bem-vindo ao Leilão Online!";
    final String template = "registration-confirmation";

    Context context = new Context();
    context.setVariable("nome", pessoa.getNome());
    context.setVariable("linkLogin", appUrl + "/login");

    enviarEmailComTemplate(pessoa.getEmail(), assunto, template, context);
  }

  private void enviarEmailComTemplate(String para, String assunto, String template, Context contexto) {
    // fallback para ambiente de desenvolvimento sem credenciais de e-mail
    if (mailUsername == null || mailUsername.isBlank()) {
      log.warn(">>>> MODO DE DESENVOLVIMENTO: E-mail não enviado. <<<<");
      log.info("Destinatário: {}", para);
      log.info("Assunto: {}", assunto);
      log.info("Variáveis do Template: {}", contexto.getVariableNames());
      return;
    }

    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

      helper.setTo(para);
      helper.setFrom(mailFrom);
      helper.setSubject(assunto);

      String html = templateEngine.process(template, contexto);
      helper.setText(html, true);

      mailSender.send(mimeMessage);
      log.info("E-mail enviado com sucesso para: {}", para);
    } catch (MessagingException e) {
      log.error("Erro ao enviar e-mail para {}: {}", para, e.getMessage());
    }
  }
}
