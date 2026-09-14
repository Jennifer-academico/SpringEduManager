package cl.miclase.springedumanager.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Evaluacion;
import cl.miclase.springedumanager.domain.Nota;
import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.repository.NotaRepository;

/**
 * Servicio encargado de la lógica de negocio relacionada con {@link Nota}.
 *
 * <p>Su operación principal, {@link #guardarOActualizar}, implementa el
 * comportamiento de "upsert": si el estudiante ya tenía una nota registrada
 * para esa evaluación, se actualiza su valor; si no la tenía, se crea una
 * nota nueva. Esto permite que el docente reutilice el mismo formulario de
 * la planilla de notas tanto para ingresar como para corregir.</p>
 */
@Service
public class NotaService {

    private final NotaRepository repository;
    private final EvaluacionService evaluacionService;
    private final PersonaService personaService;

    public NotaService(NotaRepository repository, EvaluacionService evaluacionService, PersonaService personaService) {
        this.repository = repository;
        this.evaluacionService = evaluacionService;
        this.personaService = personaService;
    }

    /**
     * Busca la nota de un estudiante en una evaluación específica.
     *
     * @param evaluacionId id de la evaluación
     * @param estudianteId id del estudiante
     * @return la nota encontrada, o vacío si aún no ha sido calificado
     */
    @Transactional(readOnly = true)
    public Optional<Nota> buscar(Long evaluacionId, Long estudianteId) {
        return repository.findByEvaluacionIdAndEstudianteId(evaluacionId, estudianteId);
    }

    /**
     * Registra la nota de un estudiante en una evaluación, creándola si
     * no existía o actualizando su valor si ya existía.
     *
     * @param evaluacionId id de la evaluación
     * @param estudianteId id del estudiante calificado
     * @param valor calificación a registrar (escala 1.0 a 7.0)
     * @return la nota creada o actualizada
     */
    @Transactional
    public Nota guardarOActualizar(Long evaluacionId, Long estudianteId, Double valor) {
        Nota nota = repository.findByEvaluacionIdAndEstudianteId(evaluacionId, estudianteId).orElse(null);
        if (nota != null) {
            nota.setValor(valor);
            return nota;
        }
        Evaluacion evaluacion = evaluacionService.obtener(evaluacionId);
        Persona estudiante = personaService.obtener(estudianteId);
        return repository.save(new Nota(valor, evaluacion, estudiante));
    }
}