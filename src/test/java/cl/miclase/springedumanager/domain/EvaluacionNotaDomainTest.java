package cl.miclase.springedumanager.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EvaluacionNotaDomainTest {

    @Test
    void evaluacion_setters_actualizanValoresCorrectamente() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        Evaluacion evaluacion = new Evaluacion("Prueba 1", curso);

        evaluacion.setNombre("Prueba 2");
        Curso nuevoCurso = new Curso("Spring", "SPRING101", "Desc2");
        evaluacion.setCurso(nuevoCurso);

        assertEquals("Prueba 2", evaluacion.getNombre());
        assertEquals(nuevoCurso, evaluacion.getCurso());
    }

    @Test
    void nota_setValor_actualizaCorrectamente() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        Evaluacion evaluacion = new Evaluacion("Prueba 1", curso);
        Persona estudiante = new Persona("Ana", "ana@clase.cl", "11111111-1", Rol.ESTUDIANTE);
        Nota nota = new Nota(5.0, evaluacion, estudiante);

        nota.setValor(6.5);

        assertEquals(6.5, nota.getValor());
        assertEquals(evaluacion, nota.getEvaluacion());
        assertEquals(estudiante, nota.getEstudiante());
    }

    @Test
    void rol_tieneLosTresValoresEsperados() {
        assertEquals(3, Rol.values().length);
        assertNotNull(Rol.valueOf("ADMIN"));
        assertNotNull(Rol.valueOf("DOCENTE"));
        assertNotNull(Rol.valueOf("ESTUDIANTE"));
    }
}