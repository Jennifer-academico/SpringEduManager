package cl.miclase.springedumanager.dto;

import cl.miclase.springedumanager.domain.Curso;

/**
 * Representación de un curso expuesta por la API REST, desacoplada de
 * la entidad JPA para no filtrar detalles internos (como la colección
 * completa de estudiantes inscritos).
 */
public record CursoApiResponse(
    Long id,
    String nombre,
    String codigo,
    String descripcion,
    String docente,
    int cantidadEstudiantes
) {
    /**
     * Construye la respuesta de API a partir de la entidad {@link Curso}.
     *
     * @param curso entidad de origen
     * @return la representación lista para serializar a JSON
     */
    public static CursoApiResponse from(Curso curso) {
        return new CursoApiResponse(
            curso.getId(),
            curso.getNombre(),
            curso.getCodigo(),
            curso.getDescripcion(),
            curso.getDocente() != null ? curso.getDocente().getNombre() : null,
            curso.getEstudiantes().size()
        );
    }
}