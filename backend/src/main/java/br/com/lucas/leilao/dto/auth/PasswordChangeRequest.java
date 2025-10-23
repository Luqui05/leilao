package br.com.lucas.leilao.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
    @NotBlank(message = "A senha atual é obrigatória.") String senhaAtual,

    @NotBlank(message = "A nova senha é obrigatória.") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$", message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.") String novaSenha) {
}
