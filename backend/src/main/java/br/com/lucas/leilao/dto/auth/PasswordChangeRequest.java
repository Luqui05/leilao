package br.com.lucas.leilao.dto.auth;

import br.com.lucas.leilao.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
    @NotBlank(message = "A senha atual é obrigatória.") String senhaAtual,

    @NotBlank(message = "A nova senha é obrigatória.") @ValidPassword String novaSenha) {
}
