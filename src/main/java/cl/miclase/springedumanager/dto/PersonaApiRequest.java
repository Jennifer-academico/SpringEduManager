package cl.miclase.springedumanager.dto;

import cl.miclase.springedumanager.domain.Rol;
import jakarta.validation.constraints.*;

/**
 * Datos recibidos por la API REST para precargar una nueva persona.
 */
public record PersonaApiRequest(
    @NotBlank String nombre,
    @NotBlank @Email String email,
    @NotBlank String rut,
    @NotNull Rol rol
) {}