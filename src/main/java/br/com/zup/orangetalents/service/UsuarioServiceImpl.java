package br.com.zup.orangetalents.service;

import br.com.zup.orangetalents.repository.UsuarioRepository;
import br.com.zup.orangetalents.service.dto.UsuarioRequest;
import br.com.zup.orangetalents.service.exception.UsuarioJaExisteException;
import br.com.zup.orangetalents.service.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioMapper usuarioMapper;

    @Override
    public UsuarioRequest save(UsuarioRequest usuarioRequest) throws UsuarioJaExisteException {
        try {
            usuarioRepository.save(usuarioMapper.usuarioRequestToUsuario(usuarioRequest));
            return usuarioRequest;
        } catch (DataIntegrityViolationException ex) {
            throw new UsuarioJaExisteException();
        }
    }
}
