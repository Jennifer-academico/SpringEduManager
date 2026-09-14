package cl.miclase.springedumanager.dto;

import java.time.LocalDate;
import cl.miclase.springedumanager.domain.Practica;

/**
 * Representación de una práctica expuesta por la API REST.
 */
public record PracticaApiResponse(
    Long id,
    String tema,
    String instrucciones,
    LocalDate fechaEntrega,
    String curso
) {
    /**
     * Construye la respuesta de API a partir de la entidad {@link Practica}.
     *
     * @param practica entidad de origen
     * @return la representación lista para serializar a JSON
     */
    public static PracticaApiResponse from(Practica practica) {
        return new PracticaApiResponse(
            practica.getId(),
            practica.getTema(),
            practica.getInstrucciones(),
            practica.getFechaEntrega(),
            practica.getCurso().getNombre()
        );
    }
}