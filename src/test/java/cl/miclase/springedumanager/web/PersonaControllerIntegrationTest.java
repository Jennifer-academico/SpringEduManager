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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarPersonas_esAccesible_paraAdmin() throws Exception {
        mockMvc.perform(get("/personas").with(user("00000000-0").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(view().name("personas"));
    }

    @Test
    void listarPersonas_esRechazado_paraRolEstudiante() throws Exception {
        mockMvc.perform(get("/personas").with(user("estudiante").roles("ESTUDIANTE")))
            .andExpect(status().isForbidden());
    }

    @Test
    void precargarPersona_conDatosValidos_redirigeAlListado() throws Exception {
        mockMvc.perform(post("/personas")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .param("nombre", "Persona Test")
                .param("email", "personatest@clase.cl")
                .param("rut", "99988877-6")
                .param("rol", "ESTUDIANTE"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/personas"));
    }
}