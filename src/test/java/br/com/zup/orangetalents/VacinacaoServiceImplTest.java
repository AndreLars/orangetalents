package br.com.zup.orangetalents;

import br.com.zup.orangetalents.repository.UsuarioRepository;
import br.com.zup.orangetalents.repository.VacinacaoRepository;
import br.com.zup.orangetalents.repository.entity.Usuario;
import br.com.zup.orangetalents.repository.entity.Vacinacao;
import br.com.zup.orangetalents.service.VacinacaoServiceImpl;
import br.com.zup.orangetalents.service.dto.VacinacaoRequest;
import br.com.zup.orangetalents.service.exception.EmailUsuarioNaoEncontrado;
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
public class VacinacaoServiceImplTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    VacinacaoRepository vacinacaoRepository;

    @InjectMocks
    VacinacaoServiceImpl vacinacaoService;

    @Test
    public void quando_salvar_deve_retornar_usuario() throws ParseException, EmailUsuarioNaoEncontrado {
        String dataVacinacaoEsperada = "19/02/2021";

        VacinacaoRequest request = new VacinacaoRequest(
                new SimpleDateFormat("dd/MM/yyyy").parse(dataVacinacaoEsperada),
                "test@email",
                "Test"
        );


        when(usuarioRepository.findByEmail(any(String.class))).thenReturn(new Usuario());
        when(vacinacaoRepository.save(any(Vacinacao.class))).thenReturn(new Vacinacao());

        VacinacaoRequest vacinacaoCriada = vacinacaoService.save(request);

        assertEquals(vacinacaoCriada.getDataVacinacao(), request.getDataVacinacao());
        assertEquals(vacinacaoCriada.getEmailUsuario(), request.getEmailUsuario());
        assertEquals(vacinacaoCriada.getNome(), request.getNome());
    }
}
