package cl.miclase.springedumanager.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CursoDomainTest {

    @Test
    void inscribir_agregaEstudianteAlConjunto() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        Persona estudiante = new Persona("Ana", "ana@clase.cl", "11111111-1", Rol.ESTUDIANTE);

        curso.inscribir(estudiante);

        assertTrue(curso.getEstudiantes().contains(estudiante));
        assertEquals(1, curso.getEstudiantes().size());
    }

    @Test
    void asignarDocente_reemplazaDocenteAnterior() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        Persona docente1 = new Persona("Ana", "ana@clase.cl", "11111111-1", Rol.DOCENTE);
        Persona docente2 = new Persona("Luis", "luis@clase.cl", "22222222-2", Rol.DOCENTE);

        curso.asignarDocente(docente1);
        assertEquals(docente1, curso.getDocente());

        curso.asignarDocente(docente2);
        assertEquals(docente2, curso.getDocente());
    }

    @Test
    void setters_actualizanValoresCorrectamente() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");

        curso.setNombre("Java Avanzado");
        curso.setCodigo("JAVA202");
        curso.setDescripcion("Nueva descripción");

        assertEquals("Java Avanzado", curso.getNombre());
        assertEquals("JAVA202", curso.getCodigo());
        assertEquals("Nueva descripción", curso.getDescripcion());
    }
}