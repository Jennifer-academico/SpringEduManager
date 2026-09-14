package cl.miclase.springedumanager.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import cl.miclase.springedumanager.domain.Curso;
import cl.miclase.springedumanager.dto.CursoApiRequest;
import cl.miclase.springedumanager.dto.CursoApiResponse;
import cl.miclase.springedumanager.service.CursoService;

/**
 * API REST para consultar y administrar cursos.
 *
 * <p>Las consultas (GET) son públicas; la creación y eliminación de
 * cursos están reservadas al rol ADMIN.</p>
 */
@RestController
@RequestMapping("/api/v1/cursos")
public class CursoRestController {

    private final CursoService service;

    public CursoRestController(CursoService service) {
        this.service = service;
    }

    /**
     * Lista todos los cursos del sistema.
     *
     * @return los cursos, representados como {@link CursoApiResponse}
     */
    @GetMapping
    public List<CursoApiResponse> listar() {
        return service.listar().stream().map(CursoApiResponse::from).toList();
    }

    /**
     * Obtiene el detalle de un curso.
     *
     * @param id id del curso
     * @return el curso encontrado, representado como {@link CursoApiResponse}
     */
    @GetMapping("/{id}")
    public CursoApiResponse obtener(@PathVariable Long id) {
        return CursoApiResponse.from(service.obtener(id));
    }

    /**
     * Crea un nuevo curso. Solo accesible para el rol ADMIN.
     *
     * @param request datos del curso a crear
     * @return el curso recién creado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CursoApiResponse crear(@Valid @RequestBody CursoApiRequest request) {
        Curso curso = service.crear(request.nombre(), request.codigo(), request.descripcion(), request.docenteId());
        return CursoApiResponse.from(curso);
    }

    /**
     * Elimina un curso. Solo accesible para el rol ADMIN.
     *
     * @param id id del curso a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}