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

import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.repository.PersonaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ActivarCuentaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaRepository personaRepository;

    @Test
    void mostrarFormulario_esAccesibleSinAutenticacion() throws Exception {
        mockMvc.perform(get("/activar-cuenta"))
            .andExpect(status().isOk())
            .andExpect(view().name("activar-cuenta"));
    }

    @Test
    void activarCuenta_conRutExistente_muestraExito() throws Exception {
        personaRepository.save(new Persona("Sin Activar", "sinactivar@clase.cl", "88877766-5", Rol.ESTUDIANTE));

        mockMvc.perform(post("/activar-cuenta")
                .with(csrf())
                .param("rut", "88877766-5")
                .param("password", "123456"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("exito"));
    }

    @Test
    void activarCuenta_conRutInexistente_muestraError() throws Exception {
        mockMvc.perform(post("/activar-cuenta")
                .with(csrf())
                .param("rut", "11122233-4")
                .param("password", "123456"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("error"));
    }
}