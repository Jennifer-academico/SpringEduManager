package cl.miclase.springedumanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Evaluacion;
import cl.miclase.springedumanager.domain.Nota;
import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.repository.NotaRepository;
import cl.miclase.springedumanager.service.EvaluacionService;
import cl.miclase.springedumanager.service.NotaService;
import cl.miclase.springedumanager.service.PersonaService;

@ExtendWith(MockitoExtension.class)
class NotaServiceTest {

    @Mock
    private NotaRepository repository;

    @Mock
    private EvaluacionService evaluacionService;

    @Mock
    private PersonaService personaService;

    private NotaService service;

    @BeforeEach
    void setUp() {
        service = new NotaService(repository, evaluacionService, personaService);
    }

    @Test
    void guardarOActualizar_creaNotaNueva_cuandoNoExisteNotaPrevia() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Evaluacion evaluacion = new Evaluacion("Prueba 1", curso);
        Persona estudiante = new Persona("Camila", "camila@clase.cl", "11111111-1", Rol.ESTUDIANTE);

        when(repository.findByEvaluacionIdAndEstudianteId(1L, 2L)).thenReturn(Optional.empty());
        when(evaluacionService.obtener(1L)).thenReturn(evaluacion);
        when(personaService.obtener(2L)).thenReturn(estudiante);
        when(repository.save(any(Nota.class))).thenAnswer(inv -> inv.getArgument(0));

        Nota resultado = service.guardarOActualizar(1L, 2L, 6.5);

        assertNotNull(resultado);
        assertEquals(6.5, resultado.getValor());
        verify(repository).save(any(Nota.class));
    }

    @Test
    void guardarOActualizar_actualizaValor_cuandoYaExisteNotaPrevia() {
        Curso curso = new Curso("Java Básico", "JAVA101", "Curso de Java");
        Evaluacion evaluacion = new Evaluacion("Prueba 1", curso);
        Persona estudiante = new Persona("Camila", "camila@clase.cl", "11111111-1", Rol.ESTUDIANTE);
        Nota notaExistente = new Nota(4.0, evaluacion, estudiante);

        when(repository.findByEvaluacionIdAndEstudianteId(1L, 2L)).thenReturn(Optional.of(notaExistente));

        Nota resultado = service.guardarOActualizar(1L, 2L, 6.8);

        assertEquals(6.8, resultado.getValor());
        assertEquals(notaExistente, resultado);
        verify(repository, never()).save(any());
        verify(evaluacionService, never()).obtener(any());
    }

    @Test
    void buscar_devuelveVacio_cuandoNoExisteNota() {
        when(repository.findByEvaluacionIdAndEstudianteId(1L, 2L)).thenReturn(Optional.empty());

        Optional<Nota> resultado = service.buscar(1L, 2L);

        assertTrue(resultado.isEmpty());
    }
}