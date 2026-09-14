package cl.miclase.springedumanager.domain;

/**
 * Roles posibles para una {@link Persona} dentro del sistema.
 *
 * <ul>
 *   <li>{@link #ADMIN}: gestiona todo el sistema — precarga personas,
 *       crea/edita cursos, y tiene acceso total.</li>
 *   <li>{@link #DOCENTE}: gestiona las prácticas y evaluaciones de los
 *       cursos que tiene asignados.</li>
 *   <li>{@link #ESTUDIANTE}: solo consulta los cursos en los que está
 *       inscrito, sus prácticas y sus propias notas.</li>
 * </ul>
 */
public enum Rol {
    ADMIN,
    DOCENTE,
    ESTUDIANTE
}