package cl.miclase.springedumanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.miclase.springedumanager.domain.Curso;

/**
 * Repositorio JPA para {@link Curso}.
 *
 * <p>Permite buscar por código, y filtrar cursos según el docente a cargo
 * o según un estudiante inscrito — bases del filtrado por rol que se
 * aplica en toda la aplicación (un docente solo ve sus cursos, un
 * estudiante solo ve los cursos en los que está inscrito).</p>
 */
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Busca un curso por su código identificador.
     *
     * @param codigo código del curso (por ejemplo, "JAVA101")
     * @return el curso encontrado, o vacío si no existe
     */
    Optional<Curso> findByCodigo(String codigo);

    /**
     * Lista los cursos a cargo de un docente específico.
     *
     * @param docenteId id de la persona (con rol DOCENTE) a cargo
     * @return cursos dictados por ese docente
     */
    List<Curso> findByDocenteId(Long docenteId);

    /**
     * Lista los cursos en los que está inscrito un estudiante específico.
     *
     * @param estudianteId id de la persona (con rol ESTUDIANTE) inscrita
     * @return cursos en los que participa ese estudiante
     */
    List<Curso> findByEstudiantesId(Long estudianteId);
}