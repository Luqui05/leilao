package br.com.lucas.leilao.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
  private static final String UPPERCASE_PATTERN = ".*[A-Z].*";
  private static final String LOWERCASE_PATTERN = ".*[a-z].*";
  private static final String DIGIT_PATTERN = ".*\\d.*";
  private static final String SPECIAL_CHAR_PATTERN = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*";
  private static final int MIN_LENGTH = 6;

  @Override
  public void initialize(ValidPassword constraintAnnotation) {
  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password == null || password.isBlank()) {
      return false;
    }

    boolean valid = true;
    StringBuilder message = new StringBuilder("A senha deve conter:");

    if (password.length() < MIN_LENGTH) {
      valid = false;
      message.append(" no mínimo ").append(MIN_LENGTH).append(" caracteres;");
    }

    if (!password.matches(UPPERCASE_PATTERN)) {
      valid = false;
      message.append(" pelo menos 1 letra maiúscula;");
    }

    if (!password.matches(LOWERCASE_PATTERN)) {
      valid = false;
      message.append(" pelo menos 1 letra minúscula;");
    }

    if (!password.matches(DIGIT_PATTERN)) {
      valid = false;
      message.append(" pelo menos 1 número;");
    }

    if (!password.matches(SPECIAL_CHAR_PATTERN)) {
      valid = false;
      message.append(" pelo menos 1 caractere especial");
    }

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message.toString())
          .addConstraintViolation();
    }

    return valid;
  }

}
