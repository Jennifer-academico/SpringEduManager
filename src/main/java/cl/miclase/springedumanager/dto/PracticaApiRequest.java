package cl.miclase.springedumanager.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

/**
 * Datos recibidos por la API REST para crear una práctica.
 */
public record PracticaApiRequest(
    @NotBlank String tema,
    @NotBlank String instrucciones,
    @NotNull LocalDate fechaEntrega,
    @NotNull Long cursoId
) {}