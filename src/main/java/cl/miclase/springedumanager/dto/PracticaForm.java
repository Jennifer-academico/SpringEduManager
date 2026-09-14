package cl.miclase.springedumanager.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

/**
 * Datos del formulario web para crear una práctica asociada a un curso.
 */
public class PracticaForm {

    @NotBlank(message = "El tema es obligatorio")
    @Size(max = 150)
    private String tema;

    @NotBlank(message = "Las instrucciones son obligatorias")
    @Size(max = 2000)
    private String instrucciones;

    @NotNull(message = "La fecha de entrega es obligatoria")
    @FutureOrPresent(message = "La fecha de entrega no puede ser en el pasado")
    private LocalDate fechaEntrega;

    @NotNull(message = "Debes seleccionar un curso")
    private Long cursoId;

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
}