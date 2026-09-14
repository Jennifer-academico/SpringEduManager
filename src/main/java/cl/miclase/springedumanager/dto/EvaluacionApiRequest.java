package cl.miclase.springedumanager.dto;

import jakarta.validation.constraints.*;

/**
 * Datos recibidos por la API REST para crear una evaluación.
 */
public record EvaluacionApiRequest(
    @NotBlank String nombre,
    @NotNull Long cursoId
) {}