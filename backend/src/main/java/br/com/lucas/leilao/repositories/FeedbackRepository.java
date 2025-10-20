package br.com.lucas.leilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}