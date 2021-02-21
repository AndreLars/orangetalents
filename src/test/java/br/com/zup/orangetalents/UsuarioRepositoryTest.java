package br.com.zup.orangetalents;

import br.com.zup.orangetalents.repository.UsuarioRepository;
import br.com.zup.orangetalents.repository.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertThrows;

@DataJpaTest
class UsuarioRepositoryTest {

    private static final int NUM_ROWS = 25;
    private static final String BASE_CPF = "->Test CPF";
    private static final String BASE_DATA = "0/02/2000";
    private static final String BASE_EMAIL = "->Test Email";
    private static final String BASE_NOME = "->Test Nome";
    @Autowired
    private TestEntityManager tem;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void inserirUsuariosTeste() throws ParseException {
        Usuario usuario;

        for (int i = 0; i < NUM_ROWS; i++) {
            usuario = new Usuario(
                    i + BASE_CPF,
                    new SimpleDateFormat("dd/MM/yyyy").parse(i + BASE_DATA),
                    i + BASE_EMAIL,
                    i + BASE_NOME
            );
            tem.persist(usuario);
        }
    }

    @Test
    void deve_lancar_excecao_no_save() throws ParseException {
        Usuario usuario = new Usuario(
                "Novo CPF",
                new SimpleDateFormat("dd/MM/yyyy").parse("19/02/2021"),
                "Novo Email",
                "Novo Nome"
        );

        usuario.setEmail(usuarioRepository.findByEmail(1 + BASE_EMAIL).getEmail());

        assertThrows(DataIntegrityViolationException.class, () -> usuarioRepository.saveAndFlush(usuario));
    }
}
