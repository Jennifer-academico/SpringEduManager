package cl.miclase.springedumanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.repository.CursoRepository;
import cl.miclase.springedumanager.service.CursoService;
import cl.miclase.springedumanager.service.PersonaService;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository repository;

    @Mock
    private PersonaService personaService;

    private CursoService service;

    @BeforeEach
    void setUp() {
        service = new CursoService(repository, personaService);
    }

    @Test
    void crear_asignaDocente_cuandoSeIndicaDocenteId() {
        Persona docente = new Persona("Ana Torres", "ana@clase.cl", "11111111-1", Rol.DOCENTE);
        when(personaService.obtener(1L)).thenReturn(docente);
        when(repository.save(any(Curso.class))).thenAnswer(inv -> inv.getArgument(0));

        Curso resultado = service.crear("Java Básico", "JAVA101", "Curso de Java", 1L);

        assertNotNull(resultado);
        assertEquals("Java Básico", resultado.getNombre());
        assertEquals(docente, resultado.getDocente());
    }

    @Test
    void crear_sinDocente_cuandoDocenteIdEsNulo() {
        when(repository.save(any(Curso.class))).thenAnswer(inv -> inv.getArgument(0));

        Curso resultado = service.crear("Java Básico", "JAVA101", "Curso de Java", null);

        assertNull(resultado.getDocente());
        verify(personaService, never()).obtener(any());
    }

    @Test
    void inscribir_agregaEstudianteAlCurso() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Persona estudiante = new Persona("Camila Rojas", "camila@clase.cl", "22222222-2", Rol.ESTUDIANTE);
        when(repository.findById(1L)).thenReturn(Optional.of(curso));
        when(personaService.obtener(2L)).thenReturn(estudiante);

        service.inscribir(1L, 2L);

        assertTrue(curso.getEstudiantes().contains(estudiante));
    }

    @Test
    void obtener_lanzaExcepcion_cuandoCursoNoExiste() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.obtener(999L));
    }

    @Test
    void listarPorDocente_delegaAlRepositorio() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        when(repository.findByDocenteId(1L)).thenReturn(List.of(curso));

        List<Curso> resultado = service.listarPorDocente(1L);

        assertEquals(1, resultado.size());
        verify(repository).findByDocenteId(1L);
    }

    @Test
    void listarPorEstudiante_delegaAlRepositorio() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        when(repository.findByEstudiantesId(2L)).thenReturn(List.of(curso));

        List<Curso> resultado = service.listarPorEstudiante(2L);

        assertEquals(1, resultado.size());
        verify(repository).findByEstudiantesId(2L);
    }
}