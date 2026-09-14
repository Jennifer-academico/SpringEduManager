package cl.miclase.springedumanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.miclase.springedumanager.domain.Practica;

/**
 * Repositorio JPA para {@link Practica}.
 *
 * <p>Además de listar las prácticas de un curso puntual, permite filtrar
 * indirectamente a través de la relación con {@code Curso}: por el docente
 * a cargo del curso, o por un estudiante inscrito en él.</p>
 */
public interface PracticaRepository extends JpaRepository<Practica, Long> {

    /**
     * Lista las prácticas de un curso específico.
     *
     * @param cursoId id del curso
     * @return prácticas asociadas a ese curso
     */
    List<Practica> findByCursoId(Long cursoId);

    /**
     * Lista las prácticas de todos los cursos a cargo de un docente.
     *
     * @param docenteId id de la persona (con rol DOCENTE) a cargo
     * @return prácticas de los cursos que dicta ese docente
     */
    List<Practica> findByCursoDocenteId(Long docenteId);

    /**
     * Lista las prácticas de todos los cursos en los que está inscrito
     * un estudiante.
     *
     * @param estudianteId id de la persona (con rol ESTUDIANTE) inscrita
     * @return prácticas de los cursos en los que participa ese estudiante
     */
    List<Practica> findByCursoEstudiantesId(Long estudianteId);
}