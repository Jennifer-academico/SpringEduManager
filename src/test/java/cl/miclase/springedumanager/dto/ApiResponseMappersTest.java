package cl.miclase.springedumanager.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import cl.miclase.springedumanager.domain.*;

class ApiResponseMappersTest {

    @Test
    void cursoApiResponse_from_conDocenteAsignado() {
        Persona docente = new Persona("Ana", "ana@clase.cl", "11111111-1", Rol.DOCENTE);
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        curso.asignarDocente(docente);

        CursoApiResponse response = CursoApiResponse.from(curso);

        assertEquals("Java", response.nombre());
        assertEquals("Ana", response.docente());
        assertEquals(0, response.cantidadEstudiantes());
    }

    @Test
    void cursoApiResponse_from_sinDocenteAsignado() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");

        CursoApiResponse response = CursoApiResponse.from(curso);

        assertNull(response.docente());
    }

    @Test
    void personaApiResponse_from_mapeaCorrectamente() {
        Persona persona = new Persona("Juan", "juan@clase.cl", "11111111-1", Rol.ESTUDIANTE);
        persona.activarCuenta("hash");

        PersonaApiResponse response = PersonaApiResponse.from(persona);

        assertEquals("Juan", response.nombre());
        assertEquals("ESTUDIANTE", response.rol());
        assertTrue(response.cuentaActiva());
    }

    @Test
    void practicaApiResponse_from_mapeaCorrectamente() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        LocalDate fecha = LocalDate.now();
        Practica practica = new Practica("Tema", "Instr", fecha, curso);

        PracticaApiResponse response = PracticaApiResponse.from(practica);

        assertEquals("Tema", response.tema());
        assertEquals("Java", response.curso());
        assertEquals(fecha, response.fechaEntrega());
    }

    @Test
    void evaluacionApiResponse_from_mapeaCorrectamente() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        Evaluacion evaluacion = new Evaluacion("Prueba 1", curso);

        EvaluacionApiResponse response = EvaluacionApiResponse.from(evaluacion);

        assertEquals("Prueba 1", response.nombre());
        assertEquals("Java", response.curso());
    }
}