package cl.miclase.springedumanager.dto;

import jakarta.validation.constraints.*;

/**
 * Datos recibidos por la API REST para crear un curso.
 */
public record CursoApiRequest(
    @NotBlank String nombre,
    @NotBlank String codigo,
    @NotBlank String descripcion,
    Long docenteId
) {}