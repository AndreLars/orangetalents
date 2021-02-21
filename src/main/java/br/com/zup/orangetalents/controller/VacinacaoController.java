package br.com.zup.orangetalents.controller;

import br.com.zup.orangetalents.service.VacinacaoService;
import br.com.zup.orangetalents.service.dto.VacinacaoRequest;
import br.com.zup.orangetalents.service.exception.EmailUsuarioNaoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class VacinacaoController {

    @Autowired
    VacinacaoService vacinacaoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("orangetalents/v1/vacinacao")
    public ResponseEntity<VacinacaoRequest> postUsuario(@Valid @RequestBody VacinacaoRequest vacinacaoRequest) {
        try {
            return new ResponseEntity<>(this.vacinacaoService.save(vacinacaoRequest), HttpStatus.CREATED);
        } catch (EmailUsuarioNaoEncontrado ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail do usuário não encontrado.", ex);
        }
    }
}
