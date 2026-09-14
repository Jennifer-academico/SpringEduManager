package cl.miclase.springedumanager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cl.miclase.springedumanager.repository.PersonaRepository;
import cl.miclase.springedumanager.repository.CursoRepository;
import cl.miclase.springedumanager.repository.PracticaRepository;
import cl.miclase.springedumanager.repository.EvaluacionRepository;
import cl.miclase.springedumanager.repository.NotaRepository;

@SpringBootTest
@ActiveProfiles("test")
class DemoDataInitializerTest {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private PracticaRepository practicaRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private NotaRepository notaRepository;

    @Test
    void datosDemo_seCarganCorrectamente_enBaseDeDatosVacia() {
        assertEquals(8, personaRepository.count());
        assertEquals(4, cursoRepository.count());
        assertEquals(5, practicaRepository.count());
        assertEquals(3, evaluacionRepository.count());
        assertEquals(5, notaRepository.count());
        assertTrue(personaRepository.findByRut("00000000-0").isPresent());
        assertTrue(personaRepository.findByRut("00000000-0").get().tieneCuentaActiva());
    }
}