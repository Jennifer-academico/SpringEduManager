package cl.miclase.springedumanager.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.domain.Persona;
import cl.miclase.springedumanager.dto.PersonaApiRequest;
import cl.miclase.springedumanager.dto.PersonaApiResponse;
import cl.miclase.springedumanager.service.PersonaService;

/**
 * API REST para consultar y administrar personas (docentes y estudiantes).
 *
 * <p>Todo este controlador está reservado al rol ADMIN, incluidas las
 * consultas: la lista de RUTs y emails registrados no debe ser visible
 * para docentes ni estudiantes.</p>
 */
@RestController
@RequestMapping("/api/v1/personas")
@PreAuthorize("hasRole('ADMIN')")
public class PersonaRestController {

    private final PersonaService service;

    public PersonaRestController(PersonaService service) {
        this.service = service;
    }

    /**
     * Lista todas las personas registradas en el sistema.
     *
     * @return las personas, representadas como {@link PersonaApiResponse}
     */
    @GetMapping
    public List<PersonaApiResponse> listar() {
        return service.listar().stream().map(PersonaApiResponse::from).toList();
    }

    /**
     * Obtiene el detalle de una persona.
     *
     * @param id id de la persona
     * @return la persona encontrada, representada como {@link PersonaApiResponse}
     */
    @GetMapping("/{id}")
    public PersonaApiResponse obtener(@PathVariable Long id) {
        return PersonaApiResponse.from(service.obtener(id));
    }

    /**
     * Precarga una nueva persona (docente o estudiante), sin cuenta
     * activa todavía.
     *
     * @param request datos de la persona a precargar
     * @return la persona recién creada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonaApiResponse crear(@Valid @RequestBody PersonaApiRequest request) {
        Persona persona = service.precargar(request.nombre(), request.email(), request.rut(), request.rol());
        return PersonaApiResponse.from(persona);
    }

    /**
     * Elimina una persona del sistema.
     *
     * @param id id de la persona a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}