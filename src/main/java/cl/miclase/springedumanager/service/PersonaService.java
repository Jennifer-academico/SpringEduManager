package cl.miclase.springedumanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.domain.Rol;
import cl.miclase.springedumanager.repository.PersonaRepository;

/**
 * Servicio encargado de la lógica de negocio relacionada con {@link Persona}.
 *
 * <p>Gestiona las dos etapas del ciclo de vida de una persona: la
 * <b>precarga</b> por parte del administrador (sin contraseña) y la
 * <b>activación de cuenta</b> por parte de la propia persona, mediante su
 * RUT y una nueva contraseña que se almacena encriptada con BCrypt.</p>
 */
@Service
public class PersonaService {

    private final PersonaRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PersonaService(PersonaRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Lista todas las personas registradas en el sistema.
     *
     * @return todas las personas, sin filtrar
     */
    @Transactional(readOnly = true)
    public List<Persona> listar() {
        return repository.findAll();
    }

    /**
     * Lista las personas que tienen un rol específico.
     *
     * @param rol rol a filtrar
     * @return personas con ese rol
     */
    @Transactional(readOnly = true)
    public List<Persona> listarPorRol(Rol rol) {
        return repository.findByRol(rol);
    }

    /**
     * Obtiene una persona por su id.
     *
     * @param id id de la persona
     * @return la persona encontrada
     * @throws IllegalArgumentException si no existe una persona con ese id
     */
    @Transactional(readOnly = true)
    public Persona obtener(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada."));
    }

    /**
     * Busca una persona por su email.
     *
     * @param email email a buscar
     * @return la persona encontrada, o vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<Persona> buscarPorEmail(String email) {
        return repository.findByEmailIgnoreCase(email);
    }

    /**
     * Busca una persona por su RUT.
     *
     * @param rut RUT a buscar
     * @return la persona encontrada, o vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<Persona> buscarPorRut(String rut) {
        return repository.findByRut(rut);
    }

    /**
     * Precarga una nueva persona en el sistema, sin cuenta activa todavía.
     * Esta operación la realiza el administrador para dar de alta a
     * docentes o estudiantes antes de que ellos mismos activen su cuenta.
     *
     * @param nombre nombre completo
     * @param email email único
     * @param rut RUT único, en formato {@code 12345678-9}
     * @param rol rol a asignar
     * @return la persona recién creada
     * @throws IllegalArgumentException si ya existe una persona con ese
     *         RUT o ese email
     */
    @Transactional
    public Persona precargar(String nombre, String email, String rut, Rol rol) {
        if (repository.findByRut(rut).isPresent()) {
            throw new IllegalArgumentException("Ya existe una persona con ese RUT.");
        }
        if (repository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException("Ya existe una persona con ese email.");
        }
        Persona persona = new Persona(nombre, email, rut, rol);
        return repository.save(persona);
    }

    /**
     * Activa la cuenta de una persona previamente precargada, definiendo
     * su contraseña.
     *
     * @param rut RUT de la persona a activar
     * @param rawPassword contraseña en texto plano elegida por la persona
     *        (se encripta antes de guardarse)
     * @return la persona con su cuenta ya activa
     * @throws IllegalArgumentException si no existe una persona con ese
     *         RUT, o si ya tiene una cuenta activa
     */
    @Transactional
    public Persona activarCuenta(String rut, String rawPassword) {
        Persona persona = repository.findByRut(rut)
            .orElseThrow(() -> new IllegalArgumentException("No existe una persona con ese RUT. Contacta al administrador."));

        if (persona.tieneCuentaActiva()) {
            throw new IllegalArgumentException("Esta persona ya tiene una cuenta activa. Inicia sesión.");
        }

        persona.activarCuenta(passwordEncoder.encode(rawPassword));
        return persona;
    }

    /**
     * Elimina una persona del sistema.
     *
     * @param id id de la persona a eliminar
     * @throws IllegalArgumentException si no existe una persona con ese id
     */
    @Transactional
    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }
}