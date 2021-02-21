package br.com.zup.orangetalents.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Usuário já existe.")
public class UsuarioJaExisteException extends Exception {
}
