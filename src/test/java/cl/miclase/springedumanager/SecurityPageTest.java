package cl.miclase.springedumanager;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_esAccesibleSinAutenticacion() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    void cursos_requiereAutenticacion() throws Exception {
        mockMvc.perform(get("/cursos"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    void crearCurso_esRechazado_paraRolEstudiante() throws Exception {
        mockMvc.perform(post("/cursos")
                .with(user("estudiante").roles("ESTUDIANTE"))
                .with(csrf())
                .param("nombre", "Test")
                .param("codigo", "TEST101")
                .param("descripcion", "Descripción de prueba"))
            .andExpect(status().isForbidden());
    }

    @Test
    void crearCurso_esPermitido_paraRolAdmin() throws Exception {
        mockMvc.perform(post("/cursos")
                .with(user("admin").roles("ADMIN"))
                .with(csrf())
                .param("nombre", "Test Admin")
                .param("codigo", "TESTADM101")
                .param("descripcion", "Descripción de prueba"))
            .andExpect(status().is3xxRedirection());
    }
}