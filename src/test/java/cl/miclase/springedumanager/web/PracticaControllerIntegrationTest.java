package cl.miclase.springedumanager.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.repository.CursoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PracticaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void listarPracticas_esAccesible_paraAdmin() throws Exception {
        mockMvc.perform(get("/practicas").with(user("00000000-0").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(view().name("practicas"));
    }

    @Test
    void crearPractica_conDatosValidos_redirigeAlListado() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso Test", "CT999", "Descripción"));

        mockMvc.perform(post("/practicas")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .param("tema", "Tema de prueba")
                .param("instrucciones", "Instrucciones de prueba")
                .param("fechaEntrega", LocalDate.now().plusDays(5).toString())
                .param("cursoId", curso.getId().toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/practicas"));
    }

    @Test
    void crearPractica_esRechazada_paraRolEstudiante() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso Test 2", "CT998", "Descripción"));

        mockMvc.perform(post("/practicas")
                .with(user("estudiante").roles("ESTUDIANTE"))
                .with(csrf())
                .param("tema", "Tema de prueba")
                .param("instrucciones", "Instrucciones de prueba")
                .param("fechaEntrega", LocalDate.now().plusDays(5).toString())
                .param("cursoId", curso.getId().toString()))
            .andExpect(status().isForbidden());
    }
}