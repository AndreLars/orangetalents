package br.com.zup.orangetalents;

import br.com.zup.orangetalents.repository.entity.Usuario;
import br.com.zup.orangetalents.service.dto.UsuarioRequest;
import br.com.zup.orangetalents.service.mapper.UsuarioMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioTest {

    private UsuarioMapperImpl usuarioMapper;

    @Before
    public void setup() {
        usuarioMapper = new UsuarioMapperImpl();
    }

    @Test
    public void cpf_deve_ser_setado_sem_pontos_e_tracos() throws ParseException {
        String cpfEsperado = "31856332020";
        String dataNascimentoEsperada = "19/02/2021";
        String emailEsperado = "test@email";
        String nomeEsperado = "Test";

        Usuario usuario = new Usuario(
                "318.563.320-20",
                new SimpleDateFormat("dd/MM/yyyy").parse("19/02/2021"),
                "test@email",
                "Test"
        );

        assertEquals(cpfEsperado, usuario.getCpf());
        assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoEsperada), usuario.getDataNascimento());
        assertEquals(emailEsperado, usuario.getEmail());
        assertEquals(nomeEsperado, usuario.getNome());
    }

    @Test
    public void cpf_deve_ser_setado_sem_pontos_e_tracos_usando_mapper() throws ParseException {
        String cpfEsperado = "31856332020";
        String dataNascimentoEsperada = "19/02/2021";
        String emailEsperado = "test@email";
        String nomeEsperado = "Test";

        UsuarioRequest usuarioRequest = new UsuarioRequest(
                "318.563.320-20",
                new SimpleDateFormat("dd/MM/yyyy").parse("19/02/2021"),
                "test@email",
                "Test"
        );

        Usuario usuario = usuarioMapper.usuarioRequestToUsuario(usuarioRequest);

        assertEquals(cpfEsperado, usuario.getCpf());
        assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoEsperada), usuario.getDataNascimento());
        assertEquals(emailEsperado, usuario.getEmail());
        assertEquals(nomeEsperado, usuario.getNome());
    }
}
