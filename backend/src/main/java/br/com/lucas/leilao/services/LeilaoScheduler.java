package br.com.lucas.leilao.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.lucas.leilao.enums.StatusLeilao;
import br.com.lucas.leilao.model.Leilao;
import br.com.lucas.leilao.repositories.LeilaoRepository;
import jakarta.transaction.Transactional;

@Component
public class LeilaoScheduler {

  @Autowired
  private LeilaoRepository leilaoRepository;

  @Scheduled(fixedRate = 6000)
  @Transactional
  public void verificarLeiloesExpirados() {
    LocalDateTime agora = LocalDateTime.now();

    // Busca leilões ABERTOS que já passaram da data/hora de fim
    List<Leilao> leiloesExpirados = leilaoRepository.findAll().stream()
        .filter(leilao -> leilao.getStatus() == StatusLeilao.ABERTO
            && leilao.getDataHoraFim().isBefore(agora))
        .toList();

    // Atualiza o status para ENCERRADO
    for (Leilao leilao : leiloesExpirados) {
      leilao.setStatus(StatusLeilao.ENCERRADO);
      leilaoRepository.save(leilao);
    }

    if (!leiloesExpirados.isEmpty()) {
      System.out.println("✅ " + leiloesExpirados.size() + " leilão(ões) encerrado(s) automaticamente.");
    }
  }
}
