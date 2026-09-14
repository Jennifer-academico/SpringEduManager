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

import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.repository.PersonaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonaApiRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaRepository personaRepository;

    @Test
    void obtenerPersonaApi_devuelveDetalle_paraAdmin() throws Exception {
        Persona persona = personaRepository.save(new Persona("Test API", "testapi@clase.cl", "77766655-4", Rol.ESTUDIANTE));

        mockMvc.perform(get("/api/v1/personas/" + persona.getId()).with(user("00000000-0").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Test API"))
            .andExpect(jsonPath("$.rol").value("ESTUDIANTE"));
    }

    @Test
    void crearPersonaApi_esPermitido_paraAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/personas")
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nueva Persona\",\"email\":\"nueva@clase.cl\",\"rut\":\"66655544-3\",\"rol\":\"DOCENTE\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.rol").value("DOCENTE"));
    }

    @Test
    void eliminarPersonaApi_esPermitido_paraAdmin() throws Exception {
        Persona persona = personaRepository.save(new Persona("A Eliminar", "aeliminar@clase.cl", "55544433-2", Rol.ESTUDIANTE));

        mockMvc.perform(delete("/api/v1/personas/" + persona.getId())
                .with(user("00000000-0").roles("ADMIN"))
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    void obtenerPersonaApi_lanza404_cuandoNoExiste() throws Exception {
        mockMvc.perform(get("/api/v1/personas/999999").with(user("00000000-0").roles("ADMIN")))
            .andExpect(status().isNotFound());
    }
}