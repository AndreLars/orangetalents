package br.com.zup.orangetalents;

import br.com.zup.orangetalents.controller.UsuarioController;
import br.com.zup.orangetalents.service.UsuarioService;
import br.com.zup.orangetalents.service.dto.UsuarioRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @MockBean
    UsuarioService usuarioService;
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deve_retornar_status_http_criado() throws Exception {
        String dataNascimentoEsperada = "19/02/2021";
        UsuarioRequest request = new UsuarioRequest();
        request.setCpf("31856332020");
        request.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoEsperada));
        request.setEmail("test@email");
        request.setNome("Test");


        when(usuarioService.save(any(UsuarioRequest.class))).thenReturn(request);

        mockMvc.perform(post("/orangetalents/v1/usuario")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value(request.getCpf()))
                .andExpect(jsonPath("$.dataNascimento").value(dataNascimentoEsperada))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.nome").value(request.getNome()));
    }
}
