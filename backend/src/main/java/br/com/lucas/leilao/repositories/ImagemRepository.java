package br.com.lucas.leilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.Imagem;

public interface ImagemRepository extends JpaRepository<Imagem, Long> {
}