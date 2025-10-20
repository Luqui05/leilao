package br.com.lucas.leilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.Lance;

public interface LanceRepository extends JpaRepository<Lance, Long> {
}