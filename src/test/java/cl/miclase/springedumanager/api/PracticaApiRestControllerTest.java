package cl.miclase.springedumanager.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.repository.CursoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PracticaApiRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void crearPracticaApi_esRechazado_paraEstudiante() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso Rechazo", "REJ999", "Desc"));

        mockMvc.perform(post("/api/v1/practicas")
                .with(user("estudiante").roles("ESTUDIANTE"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tema\":\"T\",\"instrucciones\":\"I\",\"fechaEntrega\":\""
                        + LocalDate.now().plusDays(1) + "\",\"cursoId\":" + curso.getId() + "}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void crearPracticaApi_conDatosInvalidos_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/practicas")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tema\":\"\",\"instrucciones\":\"I\",\"fechaEntrega\":\""
                        + LocalDate.now().plusDays(1) + "\",\"cursoId\":1}"))
            .andExpect(status().isBadRequest());
    }
}