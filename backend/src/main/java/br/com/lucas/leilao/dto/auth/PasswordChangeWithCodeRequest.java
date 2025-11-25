package br.com.lucas.leilao.dto.auth;

import br.com.lucas.leilao.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordChangeWithCodeRequest(
    @NotBlank(message = "O e-mail é obrigatório.") @Email(message = "Formato de e-mail inválido.") String email,

    @NotBlank(message = "O código de verificação é obrigatório.") String codigo,

    @NotBlank(message = "A nova senha é obrigatória.") @ValidPassword String novaSenha) {
}
