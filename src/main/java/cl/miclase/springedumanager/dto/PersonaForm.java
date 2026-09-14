package cl.miclase.springedumanager.dto;

import cl.miclase.springedumanager.domain.Rol;
import jakarta.validation.constraints.*;

/**
 * Datos del formulario que usa el administrador para precargar una nueva
 * persona (docente o estudiante), sin contraseña todavía.
 */
public class PersonaForm {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "\\d{7,8}-[0-9kK]", message = "El RUT debe tener el formato 12345678-9")
    private String rut;

    @NotNull(message = "Debes seleccionar un rol")
    private Rol rol;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}