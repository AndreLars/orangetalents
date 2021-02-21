package br.com.zup.orangetalents;

import br.com.zup.orangetalents.repository.UsuarioRepository;
import br.com.zup.orangetalents.repository.entity.Usuario;
import br.com.zup.orangetalents.service.UsuarioServiceImpl;
import br.com.zup.orangetalents.service.dto.UsuarioRequest;
import br.com.zup.orangetalents.service.exception.UsuarioJaExisteException;
import br.com.zup.orangetalents.service.mapper.UsuarioMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceImplTest {

    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    UsuarioMapper usuarioMapper;
    @InjectMocks
    UsuarioServiceImpl usuarioService;
    private UsuarioRepository repository;

    @Test
    public void quando_salvar_deve_retornar_usuario() throws ParseException, UsuarioJaExisteException {
        String dataNascimentoEsperada = "19/02/2021";
        UsuarioRequest request = new UsuarioRequest(
                "31856332020",
                new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoEsperada),
                "test@email",
                "Test"
        );

        when(usuarioMapper.usuarioRequestToUsuario(any(UsuarioRequest.class))).thenReturn(new Usuario());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        UsuarioRequest usuarioCriado = usuarioService.save(request);

        assertEquals(usuarioCriado.getCpf(), request.getCpf());
        assertEquals(usuarioCriado.getDataNascimento(), request.getDataNascimento());
        assertEquals(usuarioCriado.getEmail(), request.getEmail());
        assertEquals(usuarioCriado.getNome(), request.getNome());
    }

}
