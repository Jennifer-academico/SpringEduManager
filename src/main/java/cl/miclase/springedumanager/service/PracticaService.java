package cl.miclase.springedumanager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Practica;
import cl.miclase.springedumanager.repository.PracticaRepository;

/**
 * Servicio encargado de la lógica de negocio relacionada con
 * {@link Practica}.
 *
 * <p>Permite crear prácticas asociadas a un curso, y listarlas filtradas
 * según el rol de quien consulta: un administrador ve todas, un docente
 * solo las de los cursos que dicta, y un estudiante solo las de los
 * cursos en los que está inscrito.</p>
 */
@Service
public class PracticaService {

    private final PracticaRepository repository;
    private final CursoService cursoService;

    public PracticaService(PracticaRepository repository, CursoService cursoService) {
        this.repository = repository;
        this.cursoService = cursoService;
    }

    /**
     * Lista todas las prácticas del sistema, sin filtrar.
     *
     * @return todas las prácticas
     */
    @Transactional(readOnly = true)
    public List<Practica> listar() {
        return repository.findAll();
    }

    /**
     * Lista las prácticas de todos los cursos a cargo de un docente.
     *
     * @param docenteId id del docente
     * @return prácticas de los cursos que dicta ese docente
     */
    @Transactional(readOnly = true)
    public List<Practica> listarPorDocente(Long docenteId) {
        return repository.findByCursoDocenteId(docenteId);
    }

    /**
     * Lista las prácticas de todos los cursos en los que está inscrito
     * un estudiante.
     *
     * @param estudianteId id del estudiante
     * @return prácticas de los cursos en los que participa ese estudiante
     */
    @Transactional(readOnly = true)
    public List<Practica> listarPorEstudiante(Long estudianteId) {
        return repository.findByCursoEstudiantesId(estudianteId);
    }

    /**
     * Crea una nueva práctica para un curso.
     *
     * @param tema tema o título de la práctica
     * @param instrucciones detalle de lo que debe resolver el estudiante
     * @param fechaEntrega fecha límite de entrega
     * @param cursoId id del curso al que pertenece
     * @return la práctica recién creada
     */
    @Transactional
    public Practica crear(String tema, String instrucciones, LocalDate fechaEntrega, Long cursoId) {
        Curso curso = cursoService.obtener(cursoId);
        Practica practica = new Practica(tema, instrucciones, fechaEntrega, curso);
        return repository.save(practica);
    }
}