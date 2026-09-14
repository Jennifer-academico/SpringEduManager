package cl.miclase.springedumanager.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Practica;
import cl.miclase.springedumanager.repository.CursoRepository;
import cl.miclase.springedumanager.repository.PracticaRepository;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PracticaRepository practicaRepository;

    @Test
    void listarCursosApi_esPublico() throws Exception {
        mockMvc.perform(get("/api/v1/cursos"))
            .andExpect(status().isOk());
    }

    @Test
    void obtenerCursoApi_devuelveDetalle() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso API Test", "API999", "Descripción"));

        mockMvc.perform(get("/api/v1/cursos/" + curso.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Curso API Test"));
    }

    @Test
    void crearCursoApi_esRechazado_sinRolAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/cursos")
                .with(user("estudiante").roles("ESTUDIANTE"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Test\",\"codigo\":\"T1\",\"descripcion\":\"Desc\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void crearCursoApi_esPermitido_paraAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/cursos")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Curso API Nuevo\",\"codigo\":\"APINEW1\",\"descripcion\":\"Desc\"}"))
            .andExpect(status().isCreated());
    }

    @Test
    void eliminarCursoApi_esPermitido_paraAdmin() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso a eliminar", "DEL999", "Descripción"));

        mockMvc.perform(delete("/api/v1/cursos/" + curso.getId())
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    void listarPersonasApi_esRechazado_sinRolAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/personas").with(user("docente").roles("DOCENTE")))
            .andExpect(status().isForbidden());
    }

    @Test
    void listarPersonasApi_esPermitido_paraAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/personas").with(user("00000000-0").roles("ADMIN")))
            .andExpect(status().isOk());
    }

    @Test
    void listarPracticasApi_esPublico() throws Exception {
        mockMvc.perform(get("/api/v1/practicas"))
            .andExpect(status().isOk());
    }

    @Test
    void crearPracticaApi_esPermitido_paraDocente() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso Practica API", "PRACAPI1", "Descripción"));

        mockMvc.perform(post("/api/v1/practicas")
                .with(user("docente").roles("DOCENTE"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"tema\":\"Tema API\",\"instrucciones\":\"Instrucciones\",\"fechaEntrega\":\""
                        + LocalDate.now().plusDays(5) + "\",\"cursoId\":" + curso.getId() + "}"))
            .andExpect(status().isCreated());
    }

    @Test
    void listarEvaluacionesApi_esPublico() throws Exception {
        mockMvc.perform(get("/api/v1/evaluaciones"))
            .andExpect(status().isOk());
    }

    @Test
    void crearEvaluacionApi_esPermitido_paraDocente() throws Exception {
        Curso curso = cursoRepository.save(new Curso("Curso Eval API", "EVALAPI1", "Descripción"));

        mockMvc.perform(post("/api/v1/evaluaciones")
                .with(user("docente").roles("DOCENTE"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Evaluación API\",\"cursoId\":" + curso.getId() + "}"))
            .andExpect(status().isCreated());
    }
}