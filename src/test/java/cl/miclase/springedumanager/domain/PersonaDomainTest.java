package cl.miclase.springedumanager.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PersonaDomainTest {

    @Test
    void tieneCuentaActiva_esFalso_alCrearPersonaNueva() {
        Persona persona = new Persona("Juan", "juan@clase.cl", "11111111-1", Rol.ESTUDIANTE);

        assertFalse(persona.tieneCuentaActiva());
        assertNull(persona.getPasswordHash());
    }

    @Test
    void activarCuenta_estableceHashYCambiaEstado() {
        Persona persona = new Persona("Juan", "juan@clase.cl", "11111111-1", Rol.ESTUDIANTE);

        persona.activarCuenta("hash-seguro");

        assertTrue(persona.tieneCuentaActiva());
        assertEquals("hash-seguro", persona.getPasswordHash());
    }

    @Test
    void setters_actualizanValoresCorrectamente() {
        Persona persona = new Persona("Juan", "juan@clase.cl", "11111111-1", Rol.ESTUDIANTE);

        persona.setNombre("Juan Carlos");
        persona.setEmail("juancarlos@clase.cl");
        persona.setRut("99999999-9");
        persona.setRol(Rol.DOCENTE);

        assertEquals("Juan Carlos", persona.getNombre());
        assertEquals("juancarlos@clase.cl", persona.getEmail());
        assertEquals("99999999-9", persona.getRut());
        assertEquals(Rol.DOCENTE, persona.getRol());
    }
}