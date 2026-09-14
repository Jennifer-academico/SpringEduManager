package cl.miclase.springedumanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.domain.Evaluacion;
import cl.miclase.springedumanager.repository.EvaluacionRepository;

/**
 * Servicio encargado de la lógica de negocio relacionada con
 * {@link Evaluacion}.
 *
 * <p>Una Evaluación es la definición de un instrumento evaluativo para un
 * curso (por ejemplo, "Prueba 1"); las calificaciones individuales de cada
 * estudiante se gestionan por separado en {@link NotaService}.</p>
 */
@Service
public class EvaluacionService {

    private final EvaluacionRepository repository;
    private final CursoService cursoService;

    public EvaluacionService(EvaluacionRepository repository, CursoService cursoService) {
        this.repository = repository;
        this.cursoService = cursoService;
    }

    /**
     * Lista todas las evaluaciones del sistema, sin filtrar.
     *
     * @return todas las evaluaciones
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> listar() {
        return repository.findAll();
    }

    /**
     * Lista las evaluaciones definidas para un curso específico.
     *
     * @param cursoId id del curso
     * @return evaluaciones de ese curso
     */
    @Transactional(readOnly = true)
    public List<Evaluacion> listarPorCurso(Long cursoId) {
        return repository.findByCursoId(cursoId);
    }

    /**
     * Obtiene una evaluación por su id.
     *
     * @param id id de la evaluación
     * @return la evaluación encontrada
     * @throws IllegalArgumentException si no existe una evaluación con ese id
     */
    @Transactional(readOnly = true)
    public Evaluacion obtener(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada."));
    }

    /**
     * Crea una nueva evaluación para un curso.
     *
     * @param nombre nombre de la evaluación (por ejemplo, "Control 1")
     * @param cursoId id del curso al que pertenece
     * @return la evaluación recién creada
     */
    @Transactional
    public Evaluacion crear(String nombre, Long cursoId) {
        Curso curso = cursoService.obtener(cursoId);
        Evaluacion evaluacion = new Evaluacion(nombre, curso);
        return repository.save(evaluacion);
    }
}