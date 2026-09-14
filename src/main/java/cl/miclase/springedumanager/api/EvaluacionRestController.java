package cl.miclase.springedumanager.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.domain.Evaluacion;
import cl.miclase.springedumanager.dto.EvaluacionApiRequest;
import cl.miclase.springedumanager.dto.EvaluacionApiResponse;
import cl.miclase.springedumanager.service.EvaluacionService;

/**
 * API REST para consultar y crear evaluaciones.
 *
 * <p>Las consultas (GET) son públicas; la creación está reservada a
 * ADMIN y DOCENTE. Las notas asociadas a cada evaluación no se exponen
 * en esta API; se gestionan desde la interfaz web de la planilla de
 * notas.</p>
 */
@RestController
@RequestMapping("/api/v1/evaluaciones")
public class EvaluacionRestController {

    private final EvaluacionService service;

    public EvaluacionRestController(EvaluacionService service) {
        this.service = service;
    }

    /**
     * Lista todas las evaluaciones del sistema.
     *
     * @return las evaluaciones, representadas como {@link EvaluacionApiResponse}
     */
    @GetMapping
    public List<EvaluacionApiResponse> listar() {
        return service.listar().stream().map(EvaluacionApiResponse::from).toList();
    }

    /**
     * Crea una nueva evaluación. Solo accesible para ADMIN o DOCENTE.
     *
     * @param request datos de la evaluación a crear
     * @return la evaluación recién creada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public EvaluacionApiResponse crear(@Valid @RequestBody EvaluacionApiRequest request) {
        Evaluacion evaluacion = service.crear(request.nombre(), request.cursoId());
        return EvaluacionApiResponse.from(evaluacion);
    }
}