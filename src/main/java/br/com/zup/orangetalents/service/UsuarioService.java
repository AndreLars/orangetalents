package br.com.zup.orangetalents.service;

import br.com.zup.orangetalents.service.dto.UsuarioRequest;
import br.com.zup.orangetalents.service.exception.UsuarioJaExisteException;

public interface UsuarioService {
    UsuarioRequest save(UsuarioRequest usuarioRequest) throws UsuarioJaExisteException;
}
