package cl.miclase.springedumanager.dto;

import jakarta.validation.constraints.*;

/**
 * Datos del formulario web para crear una nueva evaluación (columna de
 * la planilla) asociada a un curso.
 */
public class EvaluacionForm {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "Debes seleccionar un curso")
    private Long cursoId;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
}