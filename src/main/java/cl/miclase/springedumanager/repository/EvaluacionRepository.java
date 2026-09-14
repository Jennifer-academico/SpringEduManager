package cl.miclase.springedumanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.miclase.springedumanager.domain.Evaluacion;

/**
 * Repositorio JPA para {@link Evaluacion}.
 *
 * <p>Cada evaluación pertenece a un único curso; este repositorio permite
 * obtener todas las evaluaciones definidas para un curso específico, base
 * para construir la planilla de notas.</p>
 */
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    /**
     * Lista las evaluaciones definidas para un curso específico.
     *
     * @param cursoId id del curso
     * @return evaluaciones asociadas a ese curso
     */
    List<Evaluacion> findByCursoId(Long cursoId);
}