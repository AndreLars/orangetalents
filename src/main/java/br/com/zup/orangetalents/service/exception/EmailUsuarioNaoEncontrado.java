package br.com.zup.orangetalents.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "E-mail do usuário não encontrado.")
public class EmailUsuarioNaoEncontrado extends Exception {
}
