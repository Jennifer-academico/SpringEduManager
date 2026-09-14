package cl.miclase.springedumanager.dto;

import jakarta.validation.constraints.*;

/**
 * Datos de una nota individual dentro del formulario de la planilla de
 * evaluaciones. Actualmente la planilla se procesa a través de un mapa
 * de parámetros crudos en el controlador; esta clase queda disponible
 * para escenarios donde se registre una única nota por envío.
 */
public class NotaForm {

    @NotNull(message = "La nota es obligatoria")
    @DecimalMin(value = "1.0", message = "La nota mínima es 1.0")
    @DecimalMax(value = "7.0", message = "La nota máxima es 7.0")
    private Double valor;

    @NotNull(message = "Debes seleccionar un estudiante")
    private Long estudianteId;

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
}