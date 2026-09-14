package cl.miclase.springedumanager.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
class EvaluacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void listarEvaluaciones_esAccesible_paraAdmin() throws Exception {
        mockMvc.perform(get("/evaluaciones").with(user("00000000-0").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(view().name("evaluaciones"));
    }

    @Test
    void crearEvaluacion_conDatosValidos_redirigeAlListado() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso Eval Test", "CET999", "Descripción"));

        mockMvc.perform(post("/evaluaciones")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .param("nombre", "Prueba de integración")
                .param("cursoId", curso.getId().toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/evaluaciones"));
    }

    @Test
    void guardarNotas_actualizaCorrectamente() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso Notas Test", "CNT999", "Descripción"));

        mockMvc.perform(post("/evaluaciones/curso/" + curso.getId() + "/notas")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/evaluaciones"));
    }
}