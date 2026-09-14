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
import cl.miclase.springedumanager.domain.Evaluacion;
import cl.miclase.springedumanager.repository.EvaluacionRepository;
import cl.miclase.springedumanager.service.CursoService;
import cl.miclase.springedumanager.service.EvaluacionService;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository repository;

    @Mock
    private CursoService cursoService;

    private EvaluacionService service;

    @BeforeEach
    void setUp() {
        service = new EvaluacionService(repository, cursoService);
    }

    @Test
    void crear_asociaEvaluacionAlCurso() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        when(cursoService.obtener(1L)).thenReturn(curso);
        when(repository.save(any(Evaluacion.class))).thenAnswer(inv -> inv.getArgument(0));

        Evaluacion resultado = service.crear("Prueba 1", 1L);

        assertNotNull(resultado);
        assertEquals("Prueba 1", resultado.getNombre());
        assertEquals(curso, resultado.getCurso());
    }

    @Test
    void listarPorCurso_delegaAlRepositorio() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Evaluacion evaluacion = new Evaluacion("Prueba 1", curso);
        when(repository.findByCursoId(1L)).thenReturn(List.of(evaluacion));

        List<Evaluacion> resultado = service.listarPorCurso(1L);

        assertEquals(1, resultado.size());
        verify(repository).findByCursoId(1L);
    }

    @Test
    void obtener_lanzaExcepcion_cuandoEvaluacionNoExiste() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.obtener(999L));
    }

    @Test
    void listar_devuelveTodasLasEvaluaciones() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Evaluacion eval1 = new Evaluacion("Prueba 1", curso);
        Evaluacion eval2 = new Evaluacion("Control 1", curso);
        when(repository.findAll()).thenReturn(List.of(eval1, eval2));

        List<Evaluacion> resultado = service.listar();

        assertEquals(2, resultado.size());
    }
}