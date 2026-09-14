package cl.miclase.springedumanager.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PracticaDomainTest {

    @Test
    void setters_actualizanValoresCorrectamente() {
        Curso curso = new Curso("Java", "JAVA101", "Desc");
        Practica practica = new Practica("Tema", "Instrucciones", LocalDate.now(), curso);

        practica.setTema("Nuevo tema");
        practica.setInstrucciones("Nuevas instrucciones");
        LocalDate nuevaFecha = LocalDate.now().plusDays(10);
        practica.setFechaEntrega(nuevaFecha);
        Curso nuevoCurso = new Curso("Spring", "SPRING101", "Desc2");
        practica.setCurso(nuevoCurso);

        assertEquals("Nuevo tema", practica.getTema());
        assertEquals("Nuevas instrucciones", practica.getInstrucciones());
        assertEquals(nuevaFecha, practica.getFechaEntrega());
        assertEquals(nuevoCurso, practica.getCurso());
    }
}