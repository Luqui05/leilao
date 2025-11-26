package br.com.lucas.leilao.dto.leilao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.lucas.leilao.enums.StatusLeilao;
import br.com.lucas.leilao.model.Leilao;

public record LeilaoResponse(
    Long id,
    String titulo,
    String descricao,
    String descricaoDetalhada,
    LocalDateTime dataHoraInicio,
    LocalDateTime dataHoraFim,
    StatusLeilao status,
    String observacao,
    BigDecimal valorIncremento,
    BigDecimal lanceMinimo,
    CategoriaSimpleResponse categoria,
    PublicadorSimpleResponse publicador
) {
  
  public static LeilaoResponse fromEntity(Leilao leilao) {
    return new LeilaoResponse(
        leilao.getId(),
        leilao.getTitulo(),
        leilao.getDescricao(),
        leilao.getDescricaoDetalhada(),
        leilao.getDataHoraInicio(),
        leilao.getDataHoraFim(),
        leilao.getStatus(),
        leilao.getObservacao(),
        leilao.getValorIncremento(),
        leilao.getLanceMinimo(),
        CategoriaSimpleResponse.fromEntity(leilao.getCategoria()),
        PublicadorSimpleResponse.fromEntity(leilao.getPublicador())
    );
  }

  public record CategoriaSimpleResponse(
      Long id,
      String nome,
      String observacao
  ) {
    public static CategoriaSimpleResponse fromEntity(br.com.lucas.leilao.model.Categoria categoria) {
      if (categoria == null) return null;
      return new CategoriaSimpleResponse(
          categoria.getId(),
          categoria.getNome(),
          categoria.getObservacao()
      );
    }
  }

  public record PublicadorSimpleResponse(
      Long id,
      String nome,
      String email
  ) {
    public static PublicadorSimpleResponse fromEntity(br.com.lucas.leilao.model.Pessoa pessoa) {
      if (pessoa == null) return null;
      return new PublicadorSimpleResponse(
          pessoa.getId(),
          pessoa.getNome(),
          pessoa.getEmail()
      );
    }
  }
}