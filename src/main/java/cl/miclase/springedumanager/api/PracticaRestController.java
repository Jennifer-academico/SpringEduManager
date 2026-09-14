package cl.miclase.springedumanager.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.domain.Practica;
import cl.miclase.springedumanager.dto.PracticaApiRequest;
import cl.miclase.springedumanager.dto.PracticaApiResponse;
import cl.miclase.springedumanager.service.PracticaService;

/**
 * API REST para consultar y crear prácticas.
 *
 * <p>Las consultas (GET) son públicas; la creación está reservada a
 * ADMIN y DOCENTE.</p>
 */
@RestController
@RequestMapping("/api/v1/practicas")
public class PracticaRestController {

    private final PracticaService service;

    public PracticaRestController(PracticaService service) {
        this.service = service;
    }

    /**
     * Lista todas las prácticas del sistema.
     *
     * @return las prácticas, representadas como {@link PracticaApiResponse}
     */
    @GetMapping
    public List<PracticaApiResponse> listar() {
        return service.listar().stream().map(PracticaApiResponse::from).toList();
    }

    /**
     * Crea una nueva práctica. Solo accesible para ADMIN o DOCENTE.
     *
     * @param request datos de la práctica a crear
     * @return la práctica recién creada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public PracticaApiResponse crear(@Valid @RequestBody PracticaApiRequest request) {
        Practica practica = service.crear(request.tema(), request.instrucciones(), request.fechaEntrega(), request.cursoId());
        return PracticaApiResponse.from(practica);
    }
}