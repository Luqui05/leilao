package br.com.lucas.leilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.leilao.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
