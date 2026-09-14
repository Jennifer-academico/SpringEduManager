package cl.miclase.springedumanager.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Representa a una persona dentro del sistema: puede ser un administrador,
 * un docente o un estudiante, según su {@link Rol}.
 *
 * <p>El ciclo de vida de una Persona tiene dos etapas:</p>
 * <ol>
 *   <li><b>Precarga</b>: el administrador registra los datos básicos
 *       (nombre, email, rut, rol) sin contraseña.</li>
 *   <li><b>Activación</b>: la propia persona define su contraseña mediante
 *       su RUT, momento en el cual {@code passwordHash} deja de ser nulo
 *       y la cuenta queda habilitada para iniciar sesión.</li>
 * </ol>
 */
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String rut;

    @Column(name = "password_hash", length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    protected Persona() {}

    /**
     * Crea una persona precargada por el administrador, sin cuenta activa.
     *
     * @param nombre nombre completo de la persona
     * @param email correo electrónico, único en el sistema
     * @param rut RUT en formato {@code 12345678-9}, único en el sistema
     * @param rol rol asignado (ADMIN, DOCENTE o ESTUDIANTE)
     */
    public Persona(String nombre, String email, String rut, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.rut = rut;
        this.rol = rol;
        this.passwordHash = null;
    }

    /**
     * Activa la cuenta de esta persona asignando el hash de su nueva contraseña.
     *
     * @param passwordHash contraseña ya encriptada (BCrypt)
     */
    public void activarCuenta(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Indica si la persona ya activó su cuenta (tiene contraseña definida).
     *
     * @return {@code true} si puede iniciar sesión, {@code false} si aún
     *         está solo precargada por el administrador
     */
    public boolean tieneCuentaActiva() {
        return passwordHash != null;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    @JsonIgnore public String getPasswordHash() { return passwordHash; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}