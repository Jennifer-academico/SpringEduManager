package cl.miclase.springedumanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.miclase.springedumanager.domain.Nota;

/**
 * Repositorio JPA para {@link Nota}.
 *
 * <p>Permite consultar las notas de un estudiante o de una evaluación
 * puntual, y localizar la nota exacta de un estudiante en una evaluación
 * específica — usado para decidir si se crea una nota nueva o se
 * actualiza una existente al registrar calificaciones.</p>
 */
public interface NotaRepository extends JpaRepository<Nota, Long> {

    /**
     * Lista todas las notas obtenidas por un estudiante, en cualquier curso.
     *
     * @param estudianteId id del estudiante
     * @return notas del estudiante
     */
    List<Nota> findByEstudianteId(Long estudianteId);

    /**
     * Lista todas las notas registradas para una evaluación específica.
     *
     * @param evaluacionId id de la evaluación
     * @return notas de todos los estudiantes en esa evaluación
     */
    List<Nota> findByEvaluacionId(Long evaluacionId);

    /**
     * Busca la nota de un estudiante en una evaluación específica.
     *
     * @param evaluacionId id de la evaluación
     * @param estudianteId id del estudiante
     * @return la nota encontrada, o vacío si el estudiante aún no ha sido
     *         calificado en esa evaluación
     */
    Optional<Nota> findByEvaluacionIdAndEstudianteId(Long evaluacionId, Long estudianteId);
}