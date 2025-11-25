package br.com.lucas.leilao.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.lucas.leilao.enums.TipoPerfil;
import br.com.lucas.leilao.model.Perfil;
import br.com.lucas.leilao.repositories.PerfilRepository;

@Configuration
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private PerfilRepository perfilRepository;

  @Override
  public void run(String... args) throws Exception {
    Arrays.stream(TipoPerfil.values()).forEach(tipo -> {
      if (perfilRepository.findByTipo(tipo).isEmpty()) {
        perfilRepository.save(Perfil.builder().tipo(tipo).build());
      }
    });
  }
}
