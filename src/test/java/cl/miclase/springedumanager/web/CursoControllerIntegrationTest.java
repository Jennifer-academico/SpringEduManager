package cl.miclase.springedumanager.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.repository.PersonaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CursoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void listarCursos_esAccesible_paraUsuarioAutenticado() throws Exception {
        crearDocenteActivo();

        mockMvc.perform(get("/cursos").with(user("11111111-1").roles("DOCENTE")))
            .andExpect(status().isOk())
            .andExpect(view().name("cursos"));
    }

    @Test
    void crearCurso_conDatosValidos_redirigeAlListado() throws Exception {
        mockMvc.perform(post("/cursos")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .param("nombre", "Curso de prueba")
                .param("codigo", "TEST999")
                .param("descripcion", "Descripción de prueba"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/cursos"));
    }

    @Test
    void crearCurso_conNombreVacio_vuelveAlFormularioConError() throws Exception {
        mockMvc.perform(post("/cursos")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .param("nombre", "")
                .param("codigo", "TEST998")
                .param("descripcion", "Descripción de prueba"))
            .andExpect(status().isOk())
            .andExpect(view().name("cursos"));
    }

    private void crearDocenteActivo() {
        if (personaRepository.findByRut("11111111-1").isEmpty()) {
            Persona docente = new Persona("Docente Test", "docentetest@clase.cl", "11111111-1", Rol.DOCENTE);
            docente.activarCuenta(passwordEncoder.encode("test123"));
            personaRepository.save(docente);
        }
    }
}