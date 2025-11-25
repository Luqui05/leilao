package br.com.lucas.leilao.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
  String message() default "A senha deve ter no mínimo 6 caracteres, incluindo: 1 letra maiúscula, 1 letra minúscula, 1 número e 1 caractere especial";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
