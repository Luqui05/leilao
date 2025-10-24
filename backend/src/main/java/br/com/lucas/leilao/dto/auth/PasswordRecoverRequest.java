package br.com.lucas.leilao.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordRecoverRequest(
    @NotBlank(message = "O e-mail é obrigatório") @Email(message = "Formato de e-mail inválido.") String email) {

}
