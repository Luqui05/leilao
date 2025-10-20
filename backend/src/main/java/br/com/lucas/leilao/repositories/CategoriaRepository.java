package br.com.lucas.leilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
