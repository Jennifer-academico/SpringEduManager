package cl.miclase.springedumanager.dto;

import cl.miclase.springedumanager.domain.Persona;

/**
 * Representación de una persona expuesta por la API REST. No incluye la
 * contraseña ni su hash; solo indica si la cuenta ya fue activada.
 */
public record PersonaApiResponse(
    Long id,
    String nombre,
    String email,
    String rut,
    String rol,
    boolean cuentaActiva
) {
    /**
     * Construye la respuesta de API a partir de la entidad {@link Persona}.
     *
     * @param persona entidad de origen
     * @return la representación lista para serializar a JSON
     */
    public static PersonaApiResponse from(Persona persona) {
        return new PersonaApiResponse(
            persona.getId(),
            persona.getNombre(),
            persona.getEmail(),
            persona.getRut(),
            persona.getRol().name(),
            persona.tieneCuentaActiva()
        );
    }
}