package br.com.lucas.leilao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lucas.leilao.repositories.PessoaRepository;

@Service
public class PessoaUserDetailsService implements UserDetailsService {

  @Autowired
  private PessoaRepository pessoaRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return pessoaRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
  }
}
