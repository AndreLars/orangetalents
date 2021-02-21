package br.com.zup.orangetalents;

import br.com.zup.orangetalents.controller.VacinacaoController;
import br.com.zup.orangetalents.service.VacinacaoService;
import br.com.zup.orangetalents.service.dto.VacinacaoRequest;
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
@WebMvcTest(VacinacaoController.class)
@AutoConfigureMockMvc
public class VacinacaoControllerTest {

    @MockBean
    VacinacaoService vacinacaoService;
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deve_retornar_status_http_criado() throws Exception {
        String dataVacinacaoEsperada = "19/02/2021";
        VacinacaoRequest request = new VacinacaoRequest();
        request.setEmailUsuario("test@email");
        request.setDataVacinacao(new SimpleDateFormat("dd/MM/yyyy").parse(dataVacinacaoEsperada));
        request.setNome("Test");

        when(vacinacaoService.save(any(VacinacaoRequest.class))).thenReturn(request);

        mockMvc.perform(post("/orangetalents/v1/vacinacao")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataVacinacao").value(dataVacinacaoEsperada))
                .andExpect(jsonPath("$.emailUsuario").value(request.getEmailUsuario()))
                .andExpect(jsonPath("$.nome").value(request.getNome()));
    }
}
