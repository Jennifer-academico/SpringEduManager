package cl.miclase.springedumanager.dto;

import jakarta.validation.constraints.*;

/**
 * Datos del formulario que usa una persona ya precargada para activar
 * su propia cuenta, definiendo una contraseña a partir de su RUT.
 */
public class ActivarCuentaForm {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "\\d{7,8}-[0-9kK]", message = "El RUT debe tener el formato 12345678-9")
    private String rut;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}