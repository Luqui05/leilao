package br.com.lucas.leilao.dto.categoria;

import java.util.Optional;

import jakarta.validation.constraints.Size;

/**
 * DTO para atualização parcial de Categoria.
 * - Campos Optional permitem PATCH simples sem JSON Merge Patch.
 * - Validações nos elementos quando presentes.
 */
public record CategoriaUpdateRequest(
        // campos opcionais (quando ausentes, permanecem Optional.empty())
        Optional<@Size(min = 1, max = 120, message = "Nome deve ter entre 1 e 120 caracteres") String> nome,
        Optional<@Size(max = 500, message = "Observação pode ter no máximo 500 caracteres") String> observacao
) {
    // Normaliza null -> Optional.empty() para evitar NPE no service
    public CategoriaUpdateRequest {
        nome = nome == null ? Optional.empty() : nome;
        observacao = observacao == null ? Optional.empty() : observacao;
    }

    public static CategoriaUpdateRequest empty() {
        return new CategoriaUpdateRequest(Optional.empty(), Optional.empty());
    }
}
