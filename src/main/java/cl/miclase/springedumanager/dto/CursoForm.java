package cl.miclase.springedumanager.dto;

import jakarta.validation.constraints.*;

/**
 * Datos del formulario web para crear un curso, incluyendo la
 * asignación opcional de un docente a cargo.
 */
public class CursoForm {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20)
    private String codigo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500)
    private String descripcion;

    private Long docenteId;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; }
}