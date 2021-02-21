package br.com.zup.orangetalents.controller;

import br.com.zup.orangetalents.service.UsuarioService;
import br.com.zup.orangetalents.service.dto.UsuarioRequest;
import br.com.zup.orangetalents.service.exception.UsuarioJaExisteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@Validated
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("orangetalents/v1/usuario")
    public ResponseEntity<UsuarioRequest> postUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        try {
            return new ResponseEntity<>(this.usuarioService.save(usuarioRequest), HttpStatus.CREATED);
        } catch (UsuarioJaExisteException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe.", ex);
        }
    }
}
