package br.com.lucas.leilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.Leilao;

public interface LeilaoRepository extends JpaRepository<Leilao, Long> {
}