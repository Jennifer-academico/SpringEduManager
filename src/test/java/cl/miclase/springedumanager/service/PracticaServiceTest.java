package cl.miclase.springedumanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Practica;
import cl.miclase.springedumanager.repository.PracticaRepository;
import cl.miclase.springedumanager.service.CursoService;
import cl.miclase.springedumanager.service.PracticaService;

@ExtendWith(MockitoExtension.class)
class PracticaServiceTest {

    @Mock
    private PracticaRepository repository;

    @Mock
    private CursoService cursoService;

    private PracticaService service;

    @BeforeEach
    void setUp() {
        service = new PracticaService(repository, cursoService);
    }

    @Test
    void crear_asociaPracticaAlCurso() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        LocalDate fecha = LocalDate.now().plusDays(7);
        when(cursoService.obtener(1L)).thenReturn(curso);
        when(repository.save(any(Practica.class))).thenAnswer(inv -> inv.getArgument(0));

        Practica resultado = service.crear("Variables", "Resolver ejercicios", fecha, 1L);

        assertNotNull(resultado);
        assertEquals("Variables", resultado.getTema());
        assertEquals(curso, resultado.getCurso());
        assertEquals(fecha, resultado.getFechaEntrega());
    }

    @Test
    void listarPorDocente_delegaAlRepositorio() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Practica practica = new Practica("Variables", "Instrucciones", LocalDate.now(), curso);
        when(repository.findByCursoDocenteId(1L)).thenReturn(List.of(practica));

        List<Practica> resultado = service.listarPorDocente(1L);

        assertEquals(1, resultado.size());
        verify(repository).findByCursoDocenteId(1L);
    }

    @Test
    void listarPorEstudiante_delegaAlRepositorio() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Practica practica = new Practica("Variables", "Instrucciones", LocalDate.now(), curso);
        when(repository.findByCursoEstudiantesId(2L)).thenReturn(List.of(practica));

        List<Practica> resultado = service.listarPorEstudiante(2L);

        assertEquals(1, resultado.size());
        verify(repository).findByCursoEstudiantesId(2L);
    }

    @Test
    void listar_devuelveTodasLasPracticas() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Practica practica1 = new Practica("Variables", "Instrucciones", LocalDate.now(), curso);
        Practica practica2 = new Practica("Ciclos", "Instrucciones", LocalDate.now(), curso);
        when(repository.findAll()).thenReturn(List.of(practica1, practica2));

        List<Practica> resultado = service.listar();

        assertEquals(2, resultado.size());
    }
}