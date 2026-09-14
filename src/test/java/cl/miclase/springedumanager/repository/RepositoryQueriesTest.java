package cl.miclase.springedumanager.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.*;

@SpringBootTest
@Transactional
class RepositoryQueriesTest {

    @Autowired private PersonaRepository personaRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private PracticaRepository practicaRepository;
    @Autowired private EvaluacionRepository evaluacionRepository;
    @Autowired private NotaRepository notaRepository;

    @Test
    void personaRepository_findByEmailYFindByRol_funcionanCorrectamente() {
        Persona persona = personaRepository.save(new Persona("Query Test", "querytest@clase.cl", "10101010-1", Rol.DOCENTE));

        assertTrue(personaRepository.findByEmailIgnoreCase("QUERYTEST@CLASE.CL").isPresent());
        assertTrue(personaRepository.findByRol(Rol.DOCENTE).stream().anyMatch(p -> p.getId().equals(persona.getId())));
    }

    @Test
    void cursoRepository_findByCodigoYRelaciones_funcionanCorrectamente() {
        Persona docente = personaRepository.save(new Persona("Doc Query", "docquery@clase.cl", "20202020-2", Rol.DOCENTE));
        Persona estudiante = personaRepository.save(new Persona("Est Query", "estquery@clase.cl", "30303030-3", Rol.ESTUDIANTE));

        Curso curso = new Curso("Curso Query", "QRY999", "Desc");
        curso.asignarDocente(docente);
        curso.inscribir(estudiante);
        curso = cursoRepository.save(curso);

        assertTrue(cursoRepository.findByCodigo("QRY999").isPresent());
        assertFalse(cursoRepository.findByDocenteId(docente.getId()).isEmpty());
        assertFalse(cursoRepository.findByEstudiantesId(estudiante.getId()).isEmpty());
    }

    @Test
    void practicaRepository_queriesPorCursoDocenteYEstudiante_funcionanCorrectamente() {
        Persona docente = personaRepository.save(new Persona("Doc Prac", "docprac@clase.cl", "40404040-4", Rol.DOCENTE));
        Persona estudiante = personaRepository.save(new Persona("Est Prac", "estprac@clase.cl", "50505050-5", Rol.ESTUDIANTE));
        Curso curso = new Curso("Curso Prac", "PRC999", "Desc");
        curso.asignarDocente(docente);
        curso.inscribir(estudiante);
        curso = cursoRepository.save(curso);
        practicaRepository.save(new Practica("Tema", "Instr", LocalDate.now(), curso));

        assertFalse(practicaRepository.findByCursoId(curso.getId()).isEmpty());
        assertFalse(practicaRepository.findByCursoDocenteId(docente.getId()).isEmpty());
        assertFalse(practicaRepository.findByCursoEstudiantesId(estudiante.getId()).isEmpty());
    }

    @Test
    void evaluacionYNotaRepository_queries_funcionanCorrectamente() {
        Persona estudiante = personaRepository.save(new Persona("Est Nota", "estnota@clase.cl", "60606060-6", Rol.ESTUDIANTE));
        Curso curso = cursoRepository.save(new Curso("Curso Nota", "NOT999", "Desc"));
        Evaluacion evaluacion = evaluacionRepository.save(new Evaluacion("Prueba Query", curso));
        Nota nota = notaRepository.save(new Nota(6.0, evaluacion, estudiante));

        assertFalse(evaluacionRepository.findByCursoId(curso.getId()).isEmpty());
        assertFalse(notaRepository.findByEstudianteId(estudiante.getId()).isEmpty());
        assertFalse(notaRepository.findByEvaluacionId(evaluacion.getId()).isEmpty());
        assertTrue(notaRepository.findByEvaluacionIdAndEstudianteId(evaluacion.getId(), estudiante.getId()).isPresent());
        assertEquals(nota.getId(), notaRepository.findByEvaluacionIdAndEstudianteId(evaluacion.getId(), estudiante.getId()).get().getId());
    }
}