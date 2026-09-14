package cl.miclase.springedumanager.dto;

import cl.miclase.springedumanager.domain.Evaluacion;

/**
 * Representación de una evaluación expuesta por la API REST. No incluye
 * las notas asociadas; esa información se gestiona desde la interfaz web.
 */
public record EvaluacionApiResponse(
    Long id,
    String nombre,
    String curso
) {
    /**
     * Construye la respuesta de API a partir de la entidad {@link Evaluacion}.
     *
     * @param evaluacion entidad de origen
     * @return la representación lista para serializar a JSON
     */
    public static EvaluacionApiResponse from(Evaluacion evaluacion) {
        return new EvaluacionApiResponse(
            evaluacion.getId(),
            evaluacion.getNombre(),
            evaluacion.getCurso().getNombre()
        );
    }
}